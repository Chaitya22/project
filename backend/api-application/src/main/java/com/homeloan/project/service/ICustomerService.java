package com.homeloan.project.service;

import com.homeloan.project.model.Customer;

public interface ICustomerService {
    abstract Customer addAccount(Customer customer, int seq_id);
}
