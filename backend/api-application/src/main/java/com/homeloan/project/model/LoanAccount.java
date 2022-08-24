package com.homeloan.project.model;

import javax.persistence.*;

import com.homeloan.project.helpers.CustomIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="loanAccount")
public class LoanAccount {
	@Id
	@GeneratedValue(generator = CustomIdGenerator.generatorName)
	@GenericGenerator(name = CustomIdGenerator.generatorName, strategy = "com.homeloan.project.helpers.CustomIdGenerator")
	private String loan_acc_id;
	private int seq_id;
	private double total_loan_amount;
	private double roi;
	private int tenure;

	//Valid values(Approved / Ongoing / Closed).
	private String status;
}
