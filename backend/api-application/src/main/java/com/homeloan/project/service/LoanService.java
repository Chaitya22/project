package com.homeloan.project.service;

import com.homeloan.project.http.requests.LoanRequest;
import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.repository.LoanRepository;
import exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanService implements ILoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<LoanAccount> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public LoanAccount getLoanById(String loan_acc_id) {
        Optional<LoanAccount> account = loanRepository.findById(loan_acc_id);
        if(account.isEmpty()) {
            throw new ResourceNotFoundException("No loan account exists for given id");
        }
        return account.get();
    }

    @Override
    public Boolean applyHomeLoan(LoanRequest loanRequest) {
        double eligible_loan_amount = loanRequest.getMonthly_salary() * 50;

        if(eligible_loan_amount > loanRequest.getLoan_amount()) {
            log.info("Requested amount greater than eligible amount for user <userid>");
            return false;
        }

        //Todo: Create loan account and loan repayment schedule
        //Todo: mail the user loan details and loan repayment schedule
        loanRepository.save(LoanAccount.builder()
                        .seq_id("abc")
                        .roi(7.0)
                        .status("ongoing")
                        .tenure(loanRequest.getTenure()*12)
                        .total_loan_amount(loanRequest.getLoan_amount())
                        .build()
        );
        return true;
    }
}
