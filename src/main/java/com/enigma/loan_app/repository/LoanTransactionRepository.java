package com.enigma.loan_app.repository;

import com.enigma.loan_app.entity.LoanTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, String> {
    List<LoanTransaction> findAllByCustomerId(String customerId);
}
