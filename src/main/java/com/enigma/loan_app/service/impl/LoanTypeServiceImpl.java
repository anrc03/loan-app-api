package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.entity.LoanType;
import com.enigma.loan_app.repository.LoanTypeRepository;
import com.enigma.loan_app.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTypeServiceImpl implements BaseService<LoanType, String> {

    private final LoanTypeRepository loanTypeRepository;

    @Override
    public LoanType create(LoanType loanType) {
        if (loanTypeRepository.findByType(loanType.getType()).isPresent()) return null;
        return loanTypeRepository.save(loanType);
    }

    @Override
    public List<LoanType> getAll() {
        return loanTypeRepository.findAll();
    }

    @Override
    public LoanType getById(String id) {
        return loanTypeRepository.findById(id).orElse(null);
    }

    @Override
    public LoanType update(LoanType loanType) {
        if (getById(loanType.getId()) != null) return loanTypeRepository.save(loanType);
        return null;
    }

    @Override
    public void delete(String id) {
        if (getById(id) != null) loanTypeRepository.deleteById(id);
    }
}
