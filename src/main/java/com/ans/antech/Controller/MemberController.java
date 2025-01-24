package com.ans.antech.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ans.antech.model.Member;
import com.ans.antech.service.MemberService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @Autowired
    private MemberService service;

    // 회원가입
    @PostMapping("/register.do")
    public String register(@ModelAttribute Member member) {
        System.out.println("Received Member: " + member);

        // Service를 통해 회원가입 처리
        service.registerMember(member);

        // 리다이렉트 경로 설정 (로그인 페이지로 이동 예시)
        return "redirect:/authentication-login.html";
    }

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
}
