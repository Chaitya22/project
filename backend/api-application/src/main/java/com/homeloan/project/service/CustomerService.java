package com.homeloan.project.service;

import com.homeloan.project.model.Customer;
import com.homeloan.project.model.SavingsAccount;
import com.homeloan.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService{
    @Autowired
    ISavingsAccountService savingsAccountService;
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer addAccount(Customer customer, int seq_id) {
        SavingsAccount savingsAccount = savingsAccountService.getAccountById(seq_id);
        customer.setSavingsAccount(savingsAccount);
        return customerRepository.save(customer);
    }
}
