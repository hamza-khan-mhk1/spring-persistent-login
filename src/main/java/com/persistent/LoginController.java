package com.persistent;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping(value = { "/"})
	public String home() {
		return "home";
	}

	@RequestMapping(value = { "/login" })
	public String login() {
		return "login";
	}
}