package com.ans.antech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> verificationCodes = new HashMap<>(); // 이메일과 인증 코드 저장
    private Map<String, Boolean> emailVerificationStatus = new HashMap<>(); // 이메일 인증 상태 저장

    // 랜덤 인증 코드 생성
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 랜덤 숫자
        return String.valueOf(code);
    }

    // 인증 코드 이메일 전송
    public void sendVerificationCode(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    // 인증 코드 저장
    public void storeVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            // 인증 성공 시 이메일 상태를 인증 완료로 업데이트
            emailVerificationStatus.put(email, true);
            return true;
        }
        return false;
    }

    // 이메일 인증 여부 확인
    public boolean isEmailVerified(String email) {
        return emailVerificationStatus.getOrDefault(email, false);
    }

    
}
