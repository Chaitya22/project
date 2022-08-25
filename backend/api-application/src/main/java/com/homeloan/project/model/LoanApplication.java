package com.homeloan.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="loanApplication")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String address;
    public float loan_amount;
    public int tenure;
    public float monthly_salary;
    private String status;
    private String saving_account_number;
}
