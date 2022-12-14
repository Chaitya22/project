package com.homeloan.project.service;

import com.homeloan.project.http.requests.LoanRequest;
import com.homeloan.project.mailSender.EmailSenderService;
import com.homeloan.project.model.*;
import com.homeloan.project.repository.LoanAccountRepository;
import com.homeloan.project.repository.LoanApplicationRepository;
import com.homeloan.project.repository.RepaymentRepository;
import com.homeloan.project.utils.EmiManager;
import exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanService implements ILoanService {

    @Autowired
    private LoanAccountRepository loanRepository;
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private EmiManager emiManager;
    @Autowired
    private EmailSenderService emailSenderService;

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
    public String applyHomeLoan(LoanApplication loanApplication, AuthUser customer) {
        double eligible_loan_amount = loanApplication.getMonthly_salary() * 50;
        log.info("Incoming loan request from "+ customer.getUserId());
        if(eligible_loan_amount < loanApplication.getLoan_amount()) {
            log.info("Requested amount greater than eligible amount for user "+ customer.getUserId());
            loanApplication.setStatus("Declined");
            loanApplicationRepository.save(loanApplication);
            String body = "Dear Account Holder, \n\t We regret to inform you that your application for a home loan of ??? "+ loanApplication.getLoan_amount()+" has not been approved by the bank."
                    + "\n The cause for rejection of your loan is that your requested loan amount is higher than 50 times your monthly policy. Please update the loan value and apply again."
                    + "\nThank you.\n"
                    + "\nRegards,"
                    + "\n Bank";
            emailSenderService.sendSimpleEmail(customer.getSavingsAccount().getEmail(),body,"Loan approval");
            return "Loan amount greater than eligible amount which is " + Double.toString(loanApplication.getMonthly_salary()*50);
        }

        LoanAccount savedLoan = loanRepository.save(LoanAccount.builder()
                        .savingsAccount(customer.getSavingsAccount())
                        .roi(7.0f)
                        .status("Ongoing")
                        .tenure(loanApplication.getTenure())
                        .total_loan_amount(loanApplication.getLoan_amount())
                        .build()
        );
        String body = "Dear Account Holder, \n\t We are highly pleased to inform you that your application for a home loan of ??? "+ savedLoan.getTotal_loan_amount() +" has been approved by the bank."
                + "\n Your loan account id is :" + savedLoan.getLoan_acc_id() + ". Please save it for future reference."
                + "\nThank you.\n"
                + "\nRegards,"
                + "\n Bank";
        loanApplication.setStatus("Approved");
        loanApplicationRepository.save(loanApplication);
        createRepaymentEmi(savedLoan);
        emailSenderService.sendSimpleEmail(customer.getSavingsAccount().getEmail(),body,"Loan approval");
        return "Loan sanctioned. Details of the loan are mailed.";
    }

    @Override
    public LoanApplication createApplication(LoanRequest loanRequest, AuthUser customer) {
        return loanApplicationRepository.save(LoanApplication.builder()
                        .address(loanRequest.getAddress())
                        .loan_amount(loanRequest.getLoan_amount())
                        .tenure(loanRequest.getTenure()*12)
                        .monthly_salary(loanRequest.getMonthly_salary())
                        .saving_account_number(customer.getSavingsAccount().getAccount_no())
                        .build()
        );
    }

    private void createRepaymentEmi(LoanAccount loanAccount) {

        float amt = loanAccount.getTotal_loan_amount();
        int tenure = loanAccount.getTenure();
        float emi = emiManager.CalculateEmi(amt, tenure);
        LocalDate date = LocalDate.now().plusMonths(1);
        float rate = EmiManager.INTEREST;
        for (int i = 0; i < tenure; i++) {
            float interest = (float) (amt * rate);
            float principal = emi - interest;
            Repayment rp = new Repayment();
            rp.setEmi(emi);
            rp.setInterestAmount(interest);
            rp.setPrincipalAmount(principal);
            rp.setOutstanding(amt);
            rp.setDate(date);
            rp.setStatus("Pending");
            rp.setLoanAccount(loanAccount);
            repaymentRepository.save(rp);
            amt = amt - principal;
            date = date.plusMonths(1);

        }
    }
}
