package com.homeloan.project.http.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {
    private String address;
    public double loan_amount;
    public int tenure;
    public double monthly_salary;
}
