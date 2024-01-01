package com.enigma.loan_app.repository;

import com.enigma.loan_app.constant.ERole;
import com.enigma.loan_app.entity.LoanType;
import com.enigma.loan_app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, String> {
    Optional<LoanType> findByType(String name);
}
