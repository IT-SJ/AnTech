package com.ans.antech.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ans.antech.model.Member;
import com.ans.antech.model.News;
import com.ans.antech.service.EmailService;
import com.ans.antech.service.MemberService;
import com.ans.antech.service.NewsService;
import com.ans.antech.service.SentimentService;
import com.ans.antech.service.WordCloudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ans.antech.service.HashTagService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class AnsMainController {

    @Autowired
    private MemberService service;

    @Autowired

    private EmailService emailService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private WordCloudService wordCloudService;

    // 해시태그 서비스
    @Autowired
    HashTagService hashTagService;

    // ✅ 감정 분석 서비스 추가
    @Autowired
    private SentimentService sentimentService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 변환기

    private static final Logger logger = LoggerFactory.getLogger(AnsMainController.class);

    // localhost:8080/
    @GetMapping("/")
    public String index2() {
        return "index2";
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // 회원가입
    @PostMapping("/register.join")
    public String register(@ModelAttribute Member member) {
        // 이메일 인증 여부 확인
        boolean verified = emailService.isEmailVerified(member.getEmail());
        if (!verified) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        // Service를 통해 회원가입 처리
        service.registerMember(member);

        // 회원가입 완료 후 로그인 페이지로 리다이렉트
        return "authentication-login";
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // 로그인 처리
    @PostMapping("/login.do")
    public String login(@RequestParam String id,
            @RequestParam String pw,
            HttpSession session,
            HttpServletResponse response,
            @RequestParam(required = false) boolean rememberId, Model model) {
        // ID와 PW로 사용자 조회
        Member member = service.login(id, pw);

        if (member == null) {
            // 로그인 실패 시 실패 메시지를 모델에 추가
            model.addAttribute("loginError", "입력한 정보가 틀렸습니다.");
            return "authentication-login";
        }

        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("loginMember", member);
        // 아이디 저장 처리 (쿠키에 저장)
        if (rememberId) {
            Cookie idCookie = new Cookie("rememberId", id);
            idCookie.setMaxAge(60 * 60 * 24 * 30); // 30일 동안 유지
            response.addCookie(idCookie);
        } else {
            // 기존 쿠키 삭제
            Cookie idCookie = new Cookie("rememberId", null);
            idCookie.setMaxAge(0);
            response.addCookie(idCookie);
        }

        // 메인 페이지로 이동
        return "redirect:/home";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "authentication-login";
    }

    // 이메일로 아이디 찾기 기능
    @PostMapping("/findId")
    public String findId(@RequestParam String email, RedirectAttributes redirectAttributes) {
        Member member = service.findMemberByEmail(email);
        if (member == null) {
            redirectAttributes.addFlashAttribute("findIdError", "조회된 내용이 없습니다. 회원가입을 해주세요.");
            return "redirect:/findid";
        }
        redirectAttributes.addFlashAttribute("foundId", member.getId());
        return "redirect:/findidsuccess";
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // 주요뉴스 타이틀, 워드클라우드 가져오기
    @GetMapping("/home")
    public String showNewsPage(Model model) {
        List<News> newsTitles = newsService.getAllNewsTitles(); // 뉴스 타이틀 가져오기
        List<News> breakingNewsTitles = newsService.getAllBNewsTitles();

        // 성진 - 워드 클라우드 값 가져오기 위한 List 작성 및 Map 사용
        List<String> mainNewsSummaries = wordCloudService.getAllMainNewsSummaries();
        List<String> breakingNewsSummaries = wordCloudService.getAllBreakingNewsSummaries();

        Map<String, Integer> wordFrequencies = wordCloudService.getWordFrequencies(mainNewsSummaries,
                breakingNewsSummaries);

        model.addAttribute("newsTitles", newsTitles);
        model.addAttribute("breakingNewsTitles", breakingNewsTitles);
        // 워드 클라우드 관련 모델에 담아두기
        model.addAttribute("wordFreq", wordFrequencies);

        System.out.println(wordFrequencies.toString());
        return "home";
    }

    @GetMapping("/login.do")
    public String login() {
        return "authentication-login";
    }

    @GetMapping("/register")
    public String register() {
        return "authentication-register";
    }

    @GetMapping("/tabler.do")
    public String tabler() {
        return "icon-tabler";
    }

    @GetMapping("/alerts.do")
    public String alerts() {
        return "ui-alerts";
    }

    @GetMapping("/buttons.do")
    public String buttons() {
        return "ui-buttons";
    }

    @GetMapping("/card.do")
    public String card() {
        return "ui-card";
    }

    @GetMapping("/forms.do")
    public String forms() {
        return "ui-forms";
    }

    @GetMapping("/typography.do")
    public String typography() {
        return "ui-typography";
    }

    @GetMapping("/findid")
    public String findid() {
        return "authentication-findid";
    }

    @GetMapping("/findpw")
    public String findpw() {
        return "authentication-findpw";
    }

    @GetMapping("/findidsuccess")
    public String findidsuccess() {
        return "authentication-findidsuccess";
    }

    @GetMapping("/stockinfo")
    public String stockinfo() {
        return "stock-info";
    }

    @GetMapping("/analysis")
    public String analysis() {
        return "analysis";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    // 뉴스 목록 페이지 (페이지네이션 적용)
    @GetMapping("/main")
    public String getNewsList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "latest") String sort, // ✅ 정렬 옵션 추가
            Model model) {
        int pageSize = 6; // 한 페이지당 뉴스 개수
        int totalNews = newsService.getTotalNewsCount();
        int totalPages = (int) Math.ceil((double) totalNews / pageSize);

        // 페이지가 범위를 벗어나지 않도록 제한
        if (page < 1)
            page = 1;
        if (page > totalPages)
            page = totalPages;

        // 페이지별 뉴스 가져오기 (OFFSET 적용)
        List<News> newsList;
        if ("popular".equals(sort)) {
            newsList = newsService.getNewsByViews(page, pageSize); // 조회수 기준 정렬
        } else {
            newsList = newsService.getNewsByPage(page, pageSize); // 최신순 정렬
        }

        model.addAttribute("newsList", newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort); // ✅ 현재 정렬 상태 유지

        return "main-news"; // Thymeleaf 템플릿 반환
    }

    // 검색 페이지 (페이징 적용)
    @GetMapping("/search")
    public String search(@RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        int pageSize = 6; // 한 페이지당 뉴스 개수
        int totalNews = newsService.getTotalSearchCount(keyword); // 검색 결과 개수
        int totalPages = (int) Math.ceil((double) totalNews / pageSize);

        // 검색된 뉴스 가져오기 (페이지네이션 적용)
        List<News> searchResults = newsService.getNewsByKeyword(keyword, page, pageSize);

        // 검색 결과가 없는 경우 search-no.html로 이동
        if (searchResults.isEmpty()) {
            model.addAttribute("keyword", keyword);
            return "search-no"; // 검색 결과 없음 페이지로 이동
        }

        // 모델에 데이터 추가
        model.addAttribute("keyword", keyword);
        model.addAttribute("newsList", searchResults);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "search"; // 검색 결과 페이지로 이동
    }

    // 페이지 범위 제한
    @GetMapping("/breaking")
    public String getBNewsList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "latest") String sort, // ✅ 정렬 옵션 추가
            Model model) {

        int pageSize = 6; // 한 페이지당 뉴스 개수
        int totalNews = newsService.getTotalBNewsCount();
        int totalPages = (int) Math.ceil((double) totalNews / pageSize);

        if (page < 1)
            page = 1;
        if (page > totalPages)
            page = totalPages;

        // ✅ 정렬 옵션에 따라 다르게 데이터 조회
        List<News> newsList;
        if ("popular".equals(sort)) {
            newsList = newsService.getBNewsByViews(page, pageSize); // 조회수 기준 정렬
        } else {
            newsList = newsService.getBNewsByPage(page, pageSize); // 최신순 정렬
        }

        model.addAttribute("newsList", newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort); // ✅ 현재 정렬 상태 유지

        return "breaking-news";
    }

    // 주요 뉴스 분석 페이지
    @GetMapping("/analysis/{idx}")
    public String mainAnalysis(@PathVariable int idx, Model model) {
        News news = newsService.getNewsById(idx); // 주요 뉴스 가져오기

        // 성진 - 해시태그 키워드 추출을 위한 뉴스 내용(Text) 주요뉴스, 속보뉴스에서 가져오기
        String mainNewsContent = hashTagService.getMainNewsContent(idx);

        // Flask API 호출하여 해시태그 추출
        List<String> mainHashtags = hashTagService.getMainNewsHashtags(mainNewsContent);

        // ✅ Flask API 호출하여 감정 분석 실행
        Map<String, Object> sentimentResult = sentimentService.analyzeSentiment(news.getSmr()); // 뉴스 요약(SMR) 사용

        // ✅ 첫 번째 해시태그를 검색어로 사용 (없을 경우 기본값 설정)
        String searchHashtag = mainHashtags.isEmpty() ? "" : mainHashtags.get(0).replace("#", "");

        // 🔍 디버깅 로그: 해시태그 확인
        logger.info("🔍 검색에 사용될 첫 번째 해시태그: {}", searchHashtag);

        // ✅ 관련 기사 검색 (첫 번째 해시태그 기준)
        List<News> relatedNews = newsService.findRelatedNewsByHashtag(searchHashtag, news.getTitle(), 5);

        // 🔍 디버깅 로그: 관련 기사 목록 확인
        logger.info("✅ 조회된 관련 기사 개수: {}", relatedNews.size());
        for (News article : relatedNews) {
            logger.info("📌 관련 기사: [IDX: {}, TITLE: {}]", article.getIdx(), article.getTitle());
        }

        // ✅ 뉴스 조회수 증가 (DB에서 view 값 +1)
        newsService.updateMainViewCount(idx);

        // 모델에 데이터 추가

        try {
            // ✅ JSON 문자열로 변환하여 Thymeleaf에서 올바르게 파싱 가능하도록 함
            String sentimentJson = objectMapper.writeValueAsString(sentimentResult);
            model.addAttribute("sentiment", sentimentJson);
        } catch (Exception e) {
            model.addAttribute("sentiment", "{}"); // 변환 실패 시 빈 JSON 전달
        }

        model.addAttribute("news", news);
        model.addAttribute("mainHashtags", mainHashtags);
        model.addAttribute("relatedNews", relatedNews);
        return "analysis"; // 주요 뉴스 분석 페이지
    }

    // 속보 뉴스 분석 페이지
    @GetMapping("/Banalysis/{idx}")
    public String breakingAnalysis(@PathVariable int idx, Model model) {
        News news = newsService.getBNewsById(idx); // 속보 뉴스 가져오기
        String breakingNewsContent = hashTagService.getBreakingNewsContent(idx);

        List<String> breakingHashtags = hashTagService.getBreakingNewsHashtags(breakingNewsContent);

        // ✅ Flask API 호출하여 감정 분석 실행
        Map<String, Object> sentimentResult = sentimentService.analyzeSentiment(news.getSmr()); // 뉴스 요약(SMR) 사용

        // ✅ 첫 번째 해시태그를 검색어로 사용 (없을 경우 기본값 설정)
        String searchHashtag = breakingHashtags.isEmpty() ? "" : breakingHashtags.get(0).replace("#", "");

        // 🔍 디버깅 로그: 해시태그 확인
        logger.info("🔍 검색에 사용될 첫 번째 해시태그: {}", searchHashtag);

        // ✅ 관련 기사 검색 (첫 번째 해시태그 기준)
        List<News> relatedNews = newsService.findRelatedNewsByHashtag(searchHashtag, news.getTitle(), 5);

        // 🔍 디버깅 로그: 관련 기사 목록 확인
        logger.info("✅ 조회된 관련 기사 개수: {}", relatedNews.size());
        for (News article : relatedNews) {
            logger.info("📌 관련 기사: [IDX: {}, TITLE: {}]", article.getIdx(), article.getTitle());
        }

        // ✅ 뉴스 조회수 증가 (DB에서 view 값 +1)
        newsService.updateBreakingViewCount(idx);

        // 모델에 데이터 추가

        try {
            // ✅ JSON 문자열로 변환하여 Thymeleaf에서 올바르게 파싱 가능하도록 함
            String sentimentJson = objectMapper.writeValueAsString(sentimentResult);
            model.addAttribute("sentiment", sentimentJson);
        } catch (Exception e) {
            model.addAttribute("sentiment", "{}"); // 변환 실패 시 빈 JSON 전달
        }

        model.addAttribute("news", news);
        model.addAttribute("breakingHashtags", breakingHashtags);
        model.addAttribute("relatedNews", relatedNews);
        return "Banalysis"; // 속보 뉴스 분석 페이지
    }

    @GetMapping("/Banalysis")
    public String Banalysis() {
        return "Banalysis";
    }

}