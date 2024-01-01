package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.constant.EInstallmentType;
import com.enigma.loan_app.entity.InstallmentType;
import com.enigma.loan_app.repository.InstallmentTypeRepository;
import com.enigma.loan_app.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstallmentTypeServiceImpl implements BaseService<InstallmentType, String> {

    private final InstallmentTypeRepository installmentTypeRepository;

    @Override
    public InstallmentType create(InstallmentType installmentType) {
        if (installmentTypeRepository.findByInstallmentType(installmentType.getInstallmentType()).isPresent()) return null;
        switch (installmentType.getInstallmentType().name().toLowerCase().trim()) {
            case "one_month":
                installmentType.setInstallmentType(EInstallmentType.ONE_MONTH);
                break;
            case "three_months":
                installmentType.setInstallmentType(EInstallmentType.THREE_MONTHS);
                break;
            case "six_months":
                installmentType.setInstallmentType(EInstallmentType.SIXTH_MONTHS);
                break;
            case "nine_months":
                installmentType.setInstallmentType(EInstallmentType.NINE_MONTHS);
                break;
            case "twelve_months":
                installmentType.setInstallmentType(EInstallmentType.TWELVE_MONTHS);
                break;
            default:
                installmentType.setInstallmentType(null);
        }
        if (installmentType.getInstallmentType() != null) return installmentTypeRepository.save(installmentType);
        return null;
    }

    @Override
    public List<InstallmentType> getAll() {
        return installmentTypeRepository.findAll();
    }

    @Override
    public InstallmentType getById(String id) {
        return installmentTypeRepository.findById(id).orElse(null);
    }

    @Override
    public InstallmentType update(InstallmentType installmentType) {
        if (installmentType.getId() != null) return installmentTypeRepository.save(installmentType);
        return null;
    }

    @Override
    public void delete(String id) {
        if (getById(id) != null) installmentTypeRepository.deleteById(id);
    }
}
