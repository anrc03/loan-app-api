package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.repository.LoanTransactionDetailRepository;
import com.enigma.loan_app.service.LoanTransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanTransactionDetailServiceImpl implements LoanTransactionDetailService {

    private final LoanTransactionDetailRepository loanTransactionDetailRepository;

    @Override
    public Integer getInstallmentNumber(String loanTransactionId) {
        return loanTransactionDetailRepository.getInstallmentNumber(loanTransactionId).orElse(null);
    }
}
