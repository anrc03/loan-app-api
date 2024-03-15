package com.enigma.loan_app.service;

import java.util.Optional;

public interface LoanTransactionDetailService {
    Integer getInstallmentNumber(String loanTransactionId);
}
