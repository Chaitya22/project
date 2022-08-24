package com.homeloan.project.service;

import com.homeloan.project.model.SavingsAccount;
import com.homeloan.project.repository.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SavingsAccountService implements ISavingsAccountService{
    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Override
    public List<SavingsAccount> getAllAccounts() {
        return savingsAccountRepository.findAll();
    }

    @Override
    public SavingsAccount addAccount(SavingsAccount acc) {
        int charLength = 8;
        String ac_number = String.valueOf(new Random()
                .nextInt(9 * (int) Math.pow(10, charLength - 1) - 1)
                + (int) Math.pow(10, charLength - 1));
        acc.setAccount_no(ac_number);
        return savingsAccountRepository.save(acc);
    }

    @Override
    public SavingsAccount getAccountById(int seq_id) {
        return savingsAccountRepository.findById(seq_id).orElse(null);
    }
}
