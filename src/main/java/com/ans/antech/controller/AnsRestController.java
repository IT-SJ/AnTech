package com.ans.antech.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.ans.antech.model.Member;
import com.ans.antech.service.EmailService;
import com.ans.antech.service.MemberService;
import com.ans.antech.service.StockService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private StockService stockService;

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

    @GetMapping("/kospi-kosdaq")
    public ResponseEntity<Map<String, Object>> getKospiKosdaqData() {
        return ResponseEntity.ok(stockService.getKospiKosdaqData());
    }

}
