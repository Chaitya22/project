package com.homeloan.project.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="savingsAccount")
public class SavingsAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq_id;
	private String account_no;
	private String name;
	private String email;
	private double current_balance;
}