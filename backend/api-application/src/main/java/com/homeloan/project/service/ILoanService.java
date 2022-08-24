package com.homeloan.project.service;

import com.homeloan.project.http.requests.LoanRequest;
import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.model.LoanApplication;

import java.util.List;

public interface ILoanService {
    public abstract List<LoanAccount> getAllLoans();
    public abstract LoanAccount getLoanById(String loan_acc_id);
    public abstract String applyHomeLoan(LoanApplication loanApplication);

    public abstract LoanApplication createApplication(LoanRequest loanRequest);
}
