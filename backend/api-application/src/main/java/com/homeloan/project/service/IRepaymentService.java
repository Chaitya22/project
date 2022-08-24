package com.homeloan.project.service;

import com.homeloan.project.model.Repayment;

import java.util.List;

public interface IRepaymentService {
    Repayment getEmiById(int emi_id);
    List<Repayment> getEmiByLoanId(String loan_id);
    Repayment payEmi(String loan_id) throws Exception;
    String forPayEmi(String loanId);
    String prePayEmi(String loan_id, int months);
}
