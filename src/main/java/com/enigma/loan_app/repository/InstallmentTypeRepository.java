package com.enigma.loan_app.repository;

import com.enigma.loan_app.constant.EInstallmentType;
import com.enigma.loan_app.entity.InstallmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstallmentTypeRepository extends JpaRepository<InstallmentType, String> {
    Optional<InstallmentType> findByInstallmentType(EInstallmentType name);
}
