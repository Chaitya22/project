package com.homeloan.project.http.controller;

import com.homeloan.project.http.ServiceResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class LoginController {
	
	@GetMapping("login")
	public ServiceResult<String> login() {
		return new ServiceResult<>("Logged in successfully") ;
	}
	
}
