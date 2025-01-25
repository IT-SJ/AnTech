package com.ans.antech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ans.antech.model.Member;
import com.ans.antech.service.EmailService;
import com.ans.antech.service.MemberService;


@Controller
@RequestMapping("/")
public class MainController {
    // localhost:8080/
	@GetMapping("/")
	public String index() {
		return "index";
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

	@GetMapping("/sample.do")
    public String sample() {
        return "sample-page";
    }

	@GetMapping("/login.do")
    public String login() {
        return "authentication-login";
    }

	@GetMapping("/register.do")
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

    @GetMapping("/index2.do")
    public String index2() {
        return "index2";
    }

    @GetMapping("/findid.do")
    public String findid() {
        return "authentication-findid";
    }

    @GetMapping("/findpw.do")
    public String findpw() {
        return "authentication-findpw";
    }

    @GetMapping("/findidsuccess.do")
    public String findidsuccess() {
        return "authentication-findidsuccess";
    }
    

}

