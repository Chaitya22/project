package com.homeloan.project.http.controller;

import com.homeloan.project.config.JwtTokenUtil;
import com.homeloan.project.http.ServiceResult;
import com.homeloan.project.model.Customer;
import com.homeloan.project.service.ICustomerService;
import com.homeloan.project.service.JwtUserDetailsService;
import com.homeloan.project.utils.JwtRequest;
import com.homeloan.project.utils.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	ICustomerService customerService;
	
	@PostMapping("/login")
	public ServiceResult<JwtResponse> login(@RequestBody JwtRequest authenticationRequest) throws Exception  {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return new ServiceResult<>(new JwtResponse(token));
	}

	@PostMapping(value = "/add/{seq_id}")
	public ServiceResult<?> addAccount(@RequestBody Customer customer, @PathVariable("seq_id") int seq_id) {
		try {
			return new ServiceResult<>(customerService.addAccount(customer,seq_id));
		}
		catch(Exception e){
			log.error("Error occurred while adding Account: " + e.getMessage());
			return new ServiceResult<>("Error occurred while adding Account");
		}
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
