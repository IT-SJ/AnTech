package com.ans.antech.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // localhost:8080/
	@GetMapping("/")
	public String home() {
		return "index";
	}
}
