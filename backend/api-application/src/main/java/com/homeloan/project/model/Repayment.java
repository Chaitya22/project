package com.homeloan.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="repayment")
public class Repayment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column( columnDefinition = "DATE")
	private LocalDate date;
	
	private float emi;
	private float principalAmount;
	private float interestAmount;
	private float outstanding;
	private String status;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "loan_acc_id")
	private LoanAccount loanAccount;

}
