package com.homeloan.project.http.controller;

import com.homeloan.project.http.ServiceResult;
import com.homeloan.project.http.requests.LoanRequest;
import com.homeloan.project.model.Customer;
import com.homeloan.project.model.LoanApplication;
import com.homeloan.project.service.ILoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.service.LoanService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/loans/")
public class LoanController {
	@Autowired
	ILoanService loanService;

	@GetMapping()
	public ServiceResult<List<LoanAccount>> getAllLoans() {
		List<LoanAccount> loanAccounts = loanService.getAllLoans();
		return new ServiceResult<>(loanAccounts);
	}

	@GetMapping("/{id}")
	public ServiceResult<LoanAccount> getLoanById(@PathVariable("id") String loan_acc_id) {
		LoanAccount loanAccount = loanService.getLoanById(loan_acc_id);
		return new ServiceResult<>(loanAccount);
	}
	
	@PostMapping("apply")
	public ServiceResult<String> applyHomeLoan(@RequestBody LoanRequest loanRequest, Authentication authentication) {
		Customer customer = (Customer) authentication.getPrincipal();
		LoanApplication loanApplication = loanService.createApplication(loanRequest, customer);
		String result = loanService.applyHomeLoan(loanApplication,customer);
		return new ServiceResult<>(result);
	}
}
