package com.enigma.loan_app.service;

import com.enigma.loan_app.constant.LoanStatus;
import com.enigma.loan_app.dto.request.ApproveTransactionRequest;
import com.enigma.loan_app.dto.request.LoanTransactionRequest;
import com.enigma.loan_app.entity.LoanTransaction;

import java.util.List;

public interface LoanTransactionService {
    LoanTransaction create(LoanTransactionRequest request);
    LoanTransaction approveOrRejectRequest(ApproveTransactionRequest request);
    String payInstallment(String id);
    LoanTransaction getTransactionById(String id);
    LoanStatus checkStatus(Double nominal);
    List<LoanTransaction> getAllTransactionByToken();
}
