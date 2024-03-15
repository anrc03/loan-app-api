package com.enigma.loan_app.controller;

import com.enigma.loan_app.constant.AppPath;
import com.enigma.loan_app.constant.ApprovalStatus;
import com.enigma.loan_app.dto.request.ApproveTransactionRequest;
import com.enigma.loan_app.dto.request.LoanTransactionRequest;
import com.enigma.loan_app.dto.request.PayLoanRequest;
import com.enigma.loan_app.dto.response.CommonResponse;
import com.enigma.loan_app.entity.LoanTransaction;
import com.enigma.loan_app.service.LoanTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.TRANSACTION)
@RequiredArgsConstructor
public class LoanTransactionController {

    private final LoanTransactionService loanTransactionService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> requestLoan(@RequestBody LoanTransactionRequest request) {
        LoanTransaction transaction = loanTransactionService.create(request);
        if (transaction!= null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<LoanTransaction>builder()
                            .message("Loan Request Has Been Saved")
                            .data(transaction)
                            .build()
            );
        }
        return null;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        LoanTransaction transaction = loanTransactionService.getTransactionById(id);
        if (transaction != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<LoanTransaction>builder()
                            .message("Fetch Success")
                            .data(transaction)
                            .build()
            );
        }
        return null;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllTransaction() {
        List<LoanTransaction> transaction = loanTransactionService.getAllTransactionByToken();
        if (transaction != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<List<LoanTransaction>>builder()
                            .message("Fetch Success")
                            .data(transaction)
                            .build()
            );
        }
        return null;
    }

    @PutMapping(value = "/approve")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> approveTransaction(@RequestBody ApproveTransactionRequest request) {
        LoanTransaction transaction = loanTransactionService.approveOrRejectRequest(request);
        if (transaction != null && transaction.getApprovalStatus() == ApprovalStatus.APPROVED) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<LoanTransaction>builder()
                            .message("Loan Request Approved")
                            .data(transaction)
                            .build()
            );
        }
        if (transaction != null && transaction.getApprovalStatus() == ApprovalStatus.REJECTED) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<LoanTransaction>builder()
                            .message("Loan Request Rejected")
                            .data(transaction)
                            .build()
            );
        }
        return null;
    }

    @PutMapping(value = "/pay")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> approveTransaction(@RequestBody PayLoanRequest payLoanRequest) {
        String transaction = loanTransactionService.payInstallment(payLoanRequest.getLoanTransactionId());
        if (transaction == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    CommonResponse.<String>builder()
                            .message("Payment Failed")
                            .build()
            );
        }

        else if (transaction.equals("PAID")) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<String>builder()
                            .message("Your loan is already paid off. Payment not executed.")
                            .data(transaction)
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.<String>builder()
                        .message("Payment Successful")
                        .data(transaction)
                        .build()
        );
    }
}
