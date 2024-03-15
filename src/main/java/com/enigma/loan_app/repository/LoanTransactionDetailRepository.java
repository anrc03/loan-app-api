package com.enigma.loan_app.repository;

import com.enigma.loan_app.entity.LoanTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanTransactionDetailRepository extends JpaRepository<LoanTransactionDetail, String> {
    @Query(value = "SELECT MAX(at_installment_number) FROM trx_loan_detail WHERE trx_loan_id = :loanTransactionId",
            nativeQuery = true)
    public Optional<Integer> getInstallmentNumber(String loanTransactionId);
}
