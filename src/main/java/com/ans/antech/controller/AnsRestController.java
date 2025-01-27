package com.ans.antech.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.ans.antech.model.Member;
import com.ans.antech.service.EmailService;
import com.ans.antech.service.MemberService;

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
}
