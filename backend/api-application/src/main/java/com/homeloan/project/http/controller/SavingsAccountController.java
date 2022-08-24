package com.homeloan.project.http.controller;

import com.homeloan.project.http.ServiceResult;
import com.homeloan.project.model.SavingsAccount;
import com.homeloan.project.service.ISavingsAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/savings/account/")
@Slf4j
public class SavingsAccountController {

	@Autowired
	ISavingsAccountService savingsAccountService;

	@GetMapping("/all")
	public ServiceResult<?> findAll(){
		try {
			return new ServiceResult<>(savingsAccountService.getAllAccounts());
		}
		catch(Exception e){
			log.error("Error occurred while fetching all Acount data: " + e.getMessage());
            return new ServiceResult<>("Error occurred while fetching all Savings account data");
		}
	}
	
	/**
	 * addAccount(working::)
	 * @param acc
	 * @return
	 */
	@PostMapping("/add")
	public ServiceResult<?> addAccount(@RequestBody SavingsAccount acc) {
		try {
			return new ServiceResult<>(savingsAccountService.addAccount(acc));
		}
		catch(Exception e){
			log.error("Error occurred while adding Account: " + e.getMessage());
			return new ServiceResult<>("Error occurred while adding Account");
        }
	}
}
