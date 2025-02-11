package com.ans.antech.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ans.antech.model.Member;
import com.ans.antech.model.Qna;
import com.ans.antech.service.CommoditiesService;
import com.ans.antech.service.EmailService;
import com.ans.antech.service.ExchangeRateService;
import com.ans.antech.service.MemberService;
import com.ans.antech.service.NasdaqService;
import com.ans.antech.service.QnaService;
import com.ans.antech.service.ScrapService;
import com.ans.antech.service.StockService;
import com.oreilly.servlet.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ans")
public class AnsRestController {

    @Autowired
    private MemberService service;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ScrapService scrapService;

    @Autowired
    private QnaService qnaService;

    // 회원 조회 (ID로 조회)
    @GetMapping("/{id}")
    public Member getMember(@PathVariable String id) {
        return service.getMemberById(id);
    }

    // 모든 회원 조회
    @GetMapping
    public ArrayList<Member> getAllMembers() {
        return service.getAllMembers();
    }

    // -------------------------------------------------------------------------------------
    // 성진 - 회원가입 관련 경계선 구분할께요
    // 아이디 중복 확인 API
    @GetMapping("/checkId")
    public Map<String, Boolean> checkId(@RequestParam String id) {
        boolean isAvailable = service.getMemberById(id) == null;
        System.out.println("ID: " + id + ", isAvailable: " + isAvailable);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return response;
    }

    // 인증 코드 요청
    @PostMapping("/sendCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        try {
            String verificationCode = emailService.generateVerificationCode();
            emailService.sendVerificationCode(email, "인증 코드", "인증 코드: " + verificationCode);

            // 이메일과 인증 코드를 저장
            emailService.storeVerificationCode(email, verificationCode);
            return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("인증 코드 전송 실패: " + e.getMessage());
        }
    }

    // 인증번호 확인
    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean verified = emailService.verifyCode(email, code);
        return ResponseEntity.ok(verified ? "인증 성공" : "인증 실패");
    }

    // 비밀번호 찾기
    @PostMapping("/findPw")
    public ResponseEntity<?> findPw(
            @RequestParam String id,
            @RequestParam String email,
            @RequestParam String newPassword) {

        // 이메일 인증 여부 확인
        boolean verified = emailService.isEmailVerified(email);
        if (!verified) {
            return ResponseEntity.badRequest().body("이메일 인증을 완료해주세요.");
        }

        // 아이디와 이메일이 일치하는 사용자 조회
        Member member = service.findMemberByIdAndEmail(id, email);
        if (member == null) {
            return ResponseEntity.badRequest().body("아이디와 이메일이 일치하는 사용자를 찾을 수 없습니다.");
        }

        // 비밀번호 업데이트
        service.updatePassword(id, newPassword);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // --------------------------------------------------------------------------------------------------
    // 성진 : 마이페이지 관련 여기부터 정의하세요
    @PostMapping("/profileupdate")
    public ResponseEntity<?> profileUpdate(HttpServletRequest request, HttpSession session) {
        MultipartRequest multi = null;
        String uploadDir = "C:/upload/profiles";
        int fileMaxSize = 10 * 1024 * 1024; // 10MB

        // 업로드 디렉토리가 없으면 생성
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        try {
            multi = new MultipartRequest(
                    request,
                    uploadDir,
                    fileMaxSize,
                    "UTF-8");

            String fileName = multi.getFilesystemName("profileImage");
            String userId = multi.getParameter("userId");
            // DB에는 파일 경로를 저장
            String dbPath = "/profiles/" + fileName; // 상대 경로로 저장
            boolean updateResult = service.updateProfileImage(userId, dbPath);

            if (updateResult) {
                Member updatedMember = service.getMemberById(userId);
                session.setAttribute("loginMember", updatedMember);
                return ResponseEntity.ok("프로필 이미지가 성공적으로 업데이트되었습니다.");
            }
            return ResponseEntity.badRequest().body("프로필 이미지 업데이트에 실패했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    // 코스피 & 환율 서비스 선언
    private final StockService stockService;
    private final ExchangeRateService exchangeRateService;

    // 생성자 주입 (final 필드 적용)
    public AnsRestController(StockService stockService, ExchangeRateService exchangeRateService,
            NasdaqService nasdaqService, CommoditiesService commoditiesService) {
        this.stockService = stockService;
        this.exchangeRateService = exchangeRateService;
        this.nasdaqService = nasdaqService;
        this.commoditiesService = commoditiesService;

    }

    /**
     * 코스피(KOSPI)와 코스닥(KOSDAQ) 데이터를 반환하는 API 엔드포인트
     * 
     * @return 날짜별 KOSPI & KOSDAQ 데이터
     */
    @GetMapping("/kospi-kosdaq")
    public Map<String, Object> getKospiKosdaqData() {
        return stockService.getKospiKosdaqData();
    }

    // ---------------------영빈 즐찾-----------------------

    // ✅ 특정 기사 스크랩 상태 확인 (GET)
    @GetMapping("/scrap/{idx}")
    public ResponseEntity<?> checkScrapStatus(@PathVariable int idx, @RequestParam String id) {
        boolean isScrapped = scrapService.checkScrap(id, idx);
        return ResponseEntity.ok(Collections.singletonMap("scrapped", isScrapped));
    }

    // ✅ 스크랩 추가/토글 (POST)
    @PostMapping("/scrap/{idx}")
    public ResponseEntity<?> toggleScrap(@PathVariable int idx, @RequestParam String id, @RequestParam String type) {
        try {
            // ✅ type 값 검증
            if (type == null || (!"main".equalsIgnoreCase(type) && !"breaking".equalsIgnoreCase(type))) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid type parameter"));
            }

            boolean isScrapped = scrapService.addScrap(id, idx, type); // ✅ type으로 구분
            Map<String, Boolean> response = new HashMap<>();
            response.put("scrapped", isScrapped);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "스크랩 처리 중 오류 발생: " + e.getMessage()));
        }
    }

    // ✅ 스크랩 삭제 (DELETE)
    @DeleteMapping("/scrap/{idx}")
    public ResponseEntity<?> removeScrap(@PathVariable int idx, @RequestParam String id) {
        try {
            boolean result = scrapService.removeScrap(id, idx);
            return ResponseEntity.ok(Collections.singletonMap("success", result)); // ✅ 응답 키를 `success`로 변경
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "스크랩 삭제 중 오류 발생: " + e.getMessage()));
        }
    }

    // 스크랩한 뉴스 목록 조회
    @GetMapping("/scrap-list")
    public ResponseEntity<?> getScrapNews(@RequestParam String id, @RequestParam(defaultValue = "1") int page) {
        try {
            int pageSize = 6;
            List<Map<String, Object>> newsList = scrapService.getScrapNewsByUser(id, page, pageSize);
            int totalScrapNews = scrapService.getTotalScrapNews(id);
            int totalPages = (int) Math.ceil((double) totalScrapNews / pageSize);

            Map<String, Object> response = new HashMap<>();
            response.put("newsList", newsList);
            response.put("currentPage", page);
            response.put("totalPages", totalPages);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "스크랩 뉴스 목록 조회 실패: " + e.getMessage()));
        }
    }

    /**
     * 어제 날짜 기준 환율 데이터 제공 API
     * 
     * @return 주요 통화의 최신 환율 데이터
     */
    @GetMapping("/exchange-rates")
    public Map<String, Object> getExchangeRates() {
        return exchangeRateService.getExchangeRates();
    }

    private final NasdaqService nasdaqService;

    /**
     * 나스닥, 다우존스, S&P 500 데이터를 반환하는 API 엔드포인트
     * 
     * @return 날짜별 주가지수 데이터
     */
    @GetMapping("/nasdaq-dow")
    public Map<String, Object> getNasdaqDowData() {
        return nasdaqService.getNasdaqDowData();
    }

    private final CommoditiesService commoditiesService;

    /**
     * 원자재 가격 데이터 반환 API (현재가 및 증감 포함)
     * 
     * @return 원자재별 가격 데이터
     */
    @GetMapping("/commodities")
    public Map<String, Object> getCommoditiesData() {
        return commoditiesService.getCommoditiesData();
    }

    // 영빈 qna-----------------------------------------------
    @PostMapping("/add")
    public ResponseEntity<?> addQna(@RequestBody Map<String, String> requestData, HttpSession session) {
        // 로그인된 사용자 정보 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요합니다."));
        }

        String title = requestData.get("title");
        String text = requestData.get("text");

        if (title == null || title.trim().isEmpty() || text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "제목과 내용을 입력해주세요."));
        }

        // 질문 저장
        Qna qna = new Qna();
        qna.setTitle(title);
        qna.setText(text);
        qna.setId(loginMember.getId());  // 로그인한 사용자의 ID 설정
        qnaService.addQna(qna);

        return ResponseEntity.ok(Map.of("message", "질문이 등록되었습니다."));
    }

}
