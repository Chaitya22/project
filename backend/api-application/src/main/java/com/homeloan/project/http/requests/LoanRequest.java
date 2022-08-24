package com.homeloan.project.http.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {
    private String address;
    public float loan_amount;
    public int tenure;
    public float monthly_salary;
}
