package com.poscodx.mysite.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping({"/", "/main"})
	public String index(HttpServletRequest request) {
		return "main/index";
	}
}
