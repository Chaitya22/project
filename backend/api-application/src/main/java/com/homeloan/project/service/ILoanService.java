package com.homeloan.project.service;

import com.homeloan.project.http.requests.LoanRequest;
import com.homeloan.project.model.AuthUser;
import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.model.LoanApplication;

import java.util.List;

public interface ILoanService {
    List<LoanAccount> getAllLoans();
    LoanAccount getLoanById(String loan_acc_id);
    String applyHomeLoan(LoanApplication loanApplication, AuthUser customer);

    LoanApplication createApplication(LoanRequest loanRequest, AuthUser customer);
}
