package com.homeloan.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloWorldController {

	@RequestMapping({ "/hello" })
	public String firstPage(Principal principal) {
		return "Hello World";
	}

}