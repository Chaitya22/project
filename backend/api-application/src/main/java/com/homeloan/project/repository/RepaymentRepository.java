package com.homeloan.project.repository;

import com.homeloan.project.model.LoanAccount;
import com.homeloan.project.model.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Integer> {
    @Query(value = "Select * from repayment where loan_acc_id = :loan_id and status = :status",nativeQuery = true)
    List<Repayment> findByLoanIdAndStatus(@Param("loan_id") String loan_id,@Param("status") String status);

    @Query(value = "Select * from repayment where loan_acc_id = :loan_id",nativeQuery = true)
    List<Repayment> findByLoanId(@Param("loan_id")  String loan_id);
}
