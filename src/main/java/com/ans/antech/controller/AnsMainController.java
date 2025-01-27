package com.ans.antech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ans.antech.model.Member;
import com.ans.antech.service.EmailService;
import com.ans.antech.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class AnsMainController {
    // localhost:8080/
    @GetMapping("/")
    public String index2() {
        return "index2";
    }

    @Autowired
    private MemberService service;

    @Autowired
    private EmailService emailService;

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

    // 로그인 처리
    @PostMapping("/login.do")
    public String login(@RequestParam String id,
            @RequestParam String pw,
            HttpSession session,
            HttpServletResponse response,
            @RequestParam(required = false) boolean rememberId) {
        // ID와 PW로 사용자 조회
        Member member = service.login(id, pw);

        if (member == null) {
            // 로그인 실패 시
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
        return "index";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "authentication-login";
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

    @GetMapping("/findidsuccess.do")
    public String findidsuccess() {
        return "authentication-findidsuccess";
    }

}
