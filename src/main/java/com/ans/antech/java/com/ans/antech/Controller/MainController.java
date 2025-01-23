package com.ans.antech.java.com.ans.antech.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // localhost:8080/
	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/sample.do")
    public String sample() {
        return "sample-page";
    }

	@GetMapping("/index.do")
    public String index() {
        return "index";
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


}

