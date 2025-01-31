package com.ans.antech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ans.antech.model.Member;
import com.ans.antech.model.News;
import com.ans.antech.service.EmailService;
import com.ans.antech.service.MemberService;
import com.ans.antech.service.NewsService;

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
    // 주요뉴스 타이틀 가져오기
    @GetMapping("/home")
    public String showNewsPage(Model model) {
        List<String> newsTitles = newsService.getAllNewsTitles(); // 뉴스 타이틀 가져오기
        List<String> breakingNewsTitles = newsService.getAllBNewsTitles();

        model.addAttribute("newsTitles", newsTitles);
        model.addAttribute("breakingNewsTitles", breakingNewsTitles);
        return "home";
    }

    // 검색 페이지로 이동
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        return "search"; // 검색 결과 페이지로 이동
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
    public String getNewsList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 6; // 한 페이지당 뉴스 개수
        int totalNews = newsService.getTotalNewsCount();
        int totalPages = (int) Math.ceil((double) totalNews / pageSize);

        // 페이지가 범위를 벗어나지 않도록 제한
        if (page < 1)
            page = 1;
        if (page > totalPages)
            page = totalPages;

        // 페이지별 뉴스 가져오기 (OFFSET 적용)
        List<News> newsList = newsService.getNewsByPage(page, pageSize);

        model.addAttribute("newsList", newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "main-news"; // Thymeleaf 템플릿 반환
    }

    @GetMapping("/breaking")
    public String getBNewsList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 6; // 한 페이지당 뉴스 개수
        int totalNews = newsService.getTotalBNewsCount();
        int totalPages = (int) Math.ceil((double) totalNews / pageSize);

        // 페이지가 범위를 벗어나지 않도록 제한
        if (page < 1)
            page = 1;
        if (page > totalPages)
            page = totalPages;

        // 페이지별 뉴스 가져오기 (OFFSET 적용)
        List<News> newsList = newsService.getBNewsByPage(page, pageSize);

        model.addAttribute("newsList", newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "breaking-news";
    }
}