package com.ans.antech.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(javax.servlet.http.HttpServletRequest request, Model model) {
        // 오류 코드 가져오기
        Object statusCode = request.getAttribute(javax.servlet.RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(javax.servlet.RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("status", statusCode != null ? statusCode.toString() : "Unknown");
        model.addAttribute("message", errorMessage != null ? errorMessage.toString() : "알 수 없는 오류가 발생했습니다.");

        return "error"; // error.html 반환
    }
}