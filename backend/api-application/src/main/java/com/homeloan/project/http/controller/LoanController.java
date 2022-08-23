package com.homeloan.project.http.controller;

import com.homeloan.project.http.ServiceResult;
import com.homeloan.project.http.requests.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans/")
public class LoanController {
	@Autowired
	LoanService loanService;

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
	public ServiceResult<String> applyHomeLoan(@RequestBody LoanRequest loanRequest) {
		Boolean result = loanService.applyHomeLoan(loanRequest);
		if(result) {
			return new ServiceResult<>("Loan sanctioned. Details of the loan are mailed.");
		} else {
			return new ServiceResult<>("Loan amount greater than eligible amount which is " + Double.toString(loanRequest.getMonthly_salary()*50));
		}
	}
}
