package com.homeloan.project.service;

import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.model.Repayment;
import com.homeloan.project.model.SavingsAccount;
import com.homeloan.project.repository.LoanAccountRepository;
import com.homeloan.project.repository.RepaymentRepository;
import com.homeloan.project.repository.SavingsAccountRepository;
import com.homeloan.project.utils.EmiManager;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RepaymentService implements IRepaymentService {

    @Autowired
    RepaymentRepository repaymentRepository;
    @Autowired
    LoanAccountRepository loanAccountRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private EmiManager emiManager;

    @Override
    public Repayment getEmiById(int emi_id) {
        Optional<Repayment> repayment = repaymentRepository.findById(emi_id);
        if(repayment.isEmpty()) {
            throw new ResourceNotFoundException("No Emi with given id available");
        }
        return repayment.get();
    }

    @Override
    public List<Repayment> getEmiByLoanId(String loan_id) {
        LoanAccount loanAccount = loanAccountRepository.findById(loan_id).orElse(null);
        if(loanAccount == null) {
            throw new ResourceNotFoundException("No Loan account available for given Loan Account Id");
        }
        return repaymentRepository.findByLoanId(loan_id);
    }

    @Override
    public Repayment payEmi(String loan_id) throws Exception {
        LoanAccount loanAccount = loanAccountRepository.findById(loan_id).orElse(null);
        if(loanAccount == null) {
            throw new ResourceNotFoundException("No Loan account available for given Loan Account Id");
        }
        List<Repayment> lst = repaymentRepository.findByLoanIdAndStatus(loan_id, "Pending");
        if(lst.size() == 0) {
            throw new Exception("No remaining emi's for the given loan");
        }
        Repayment repay = lst.get(0);

        SavingsAccount acc = loanAccount.getSavingsAccount();
        float balance = acc.getCurrent_balance();
        float deductAmount = repay.getEmi();
        acc.setCurrent_balance(balance - deductAmount);
        repay.setStatus("Paid");
        repaymentRepository.save(repay);
        if(lst.size() == 1) {
            loanAccount.setStatus("Closed");
            loanAccountRepository.save(loanAccount);
        }
        savingsAccountRepository.save(acc);

        return repay;
    }

    @Override
    public String forPayEmi(String loan_id) {
        LoanAccount loanAccount = loanAccountRepository.findById(loan_id).orElse(null);
        if(loanAccount == null) {
            throw new ResourceNotFoundException("No Loan account available for given Loan Account Id");
        }

        List<Repayment> paidList = repaymentRepository.findByLoanIdAndStatus(loan_id, "Paid");
        if (paidList.size() < 3) {
            return "Loan foreclosure forbidden !";
        }
        List<Repayment> unPaidEmiList = repaymentRepository.findByLoanIdAndStatus(loan_id, "Pending");
        Repayment unPaidEmi = unPaidEmiList.get(0);
        unPaidEmiList.remove(0);
        int outstanding = (int) unPaidEmi.getOutstanding();
        unPaidEmi.setInterestAmount(0);
        unPaidEmi.setPrincipalAmount(0);
        unPaidEmi.setEmi(outstanding);
        unPaidEmi.setDate(LocalDate.now());
        unPaidEmi.setStatus("Paid");
        repaymentRepository.save(unPaidEmi);
        for (Repayment r : unPaidEmiList) {
            r.setStatus("Cancelled");
            repaymentRepository.save(r);
        }
        SavingsAccount acc = loanAccount.getSavingsAccount();
        float currBalance = acc.getCurrent_balance();
        acc.setCurrent_balance(currBalance - outstanding);
        savingsAccountRepository.save(acc);
        loanAccount.setStatus("Closed");
        loanAccountRepository.save(loanAccount);

        return "Foreclosed Successfully";
    }

    @Override
    public String prePayEmi(String loan_id, int months) {
        LoanAccount loanAccount = loanAccountRepository.findById(loan_id).orElse(null);
        if(loanAccount == null) {
            throw new ResourceNotFoundException("No Loan account available for given Loan Account Id");
        }


        SavingsAccount acc = loanAccount.getSavingsAccount();
        List<Repayment> lst = repaymentRepository.findByLoanIdAndStatus(loan_id, "Pending");
        Repayment repay = lst.get(0);
        if(lst.size()<months || months<3) {
            return "please enter correct tenure in months. Minimum value 3 ";
        }
        if(lst.size()==months ) {
            return "Please enter tenure between 3 and "+ (loanAccount.getTenure() - lst.size());
        }

        float totalEmi = repay.getEmi()*months;
        float interest = repay.getInterestAmount();
        float newOutstanding = repay.getOutstanding()+interest-totalEmi;
        repay.setPrincipalAmount(totalEmi-interest);
        repay.setEmi(totalEmi);
        repay.setStatus("Paid");
        repaymentRepository.save(repay);
        float balance = acc.getCurrent_balance();
        acc.setCurrent_balance(balance-totalEmi);
        savingsAccountRepository.save(acc);

        lst.remove(0);
        float newEmi = emiManager.CalculateEmi(newOutstanding,lst.size() );
        float rate = EmiManager.INTEREST;
        for(Repayment rp: lst) {
            interest = newOutstanding * rate;
            float principal = newEmi - interest;
            rp.setEmi(newEmi);
            rp.setInterestAmount(interest);
            rp.setPrincipalAmount(principal);
            rp.setOutstanding(newOutstanding);
            repaymentRepository.save(rp);
            newOutstanding = newOutstanding-principal;

        }
        return "Emi Paid successfully for " + months + " months";
    }
}
