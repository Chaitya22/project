package com.homeloan.project.service;

import com.homeloan.project.http.requests.LoanRequest;
import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.model.LoanApplication;
import com.homeloan.project.repository.LoanAccountRepository;
import com.homeloan.project.repository.LoanApplicationRepository;
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
    private LoanAccountRepository loanRepository;
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

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
    public String applyHomeLoan(LoanApplication loanApplication) {
        double eligible_loan_amount = loanApplication.getMonthly_salary() * 50;

        if(eligible_loan_amount < loanApplication.getLoan_amount()) {
            log.info("Requested amount greater than eligible amount for user <userid>");
            loanApplication.setStatus("Declined");
            loanApplicationRepository.save(loanApplication);
            return "Loan amount greater than eligible amount which is " + Double.toString(loanApplication.getMonthly_salary()*50);
        }

        loanRepository.save(LoanAccount.builder()
                        .seq_id("abc")
                        .roi(7.0f)
                        .status("Ongoing")
                        .tenure(loanApplication.getTenure())
                        .total_loan_amount(loanApplication.getLoan_amount())
                        .build()
        );
        loanApplication.setStatus("Approved");
        loanApplicationRepository.save(loanApplication);
        //Todo: mail the user loan details and loan repayment schedule
        return "Loan sanctioned. Details of the loan are mailed.";
    }

    @Override
    public LoanApplication createApplication(LoanRequest loanRequest) {
        return loanApplicationRepository.save(LoanApplication.builder()
                        .address(loanRequest.getAddress())
                        .loan_amount(loanRequest.getLoan_amount())
                        .tenure(loanRequest.getTenure()*12)
                        .monthly_salary(loanRequest.getMonthly_salary())
                        .saving_account_number("abc")
                        .build()
        );
    }
}
