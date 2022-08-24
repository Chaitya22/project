package com.homeloan.project.service;

import com.homeloan.project.model.SavingsAccount;

import java.util.List;

public interface ISavingsAccountService {
    abstract List<SavingsAccount> getAllAccounts();
    abstract SavingsAccount addAccount(SavingsAccount acc);

    abstract SavingsAccount getAccountById(int seq_id);
}
