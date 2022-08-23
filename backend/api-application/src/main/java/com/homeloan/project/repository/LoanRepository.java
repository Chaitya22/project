package com.homeloan.project.repository;

import com.homeloan.project.model.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanAccount, String> {
}
