package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.constant.ApprovalStatus;
import com.enigma.loan_app.constant.EInstallmentType;
import com.enigma.loan_app.constant.LoanStatus;
import com.enigma.loan_app.dto.request.ApproveTransactionRequest;
import com.enigma.loan_app.dto.request.LoanTransactionRequest;
import com.enigma.loan_app.dto.request.PayInstallmentRequest;
import com.enigma.loan_app.entity.Customer;
import com.enigma.loan_app.entity.LoanTransaction;
import com.enigma.loan_app.entity.LoanTransactionDetail;
import com.enigma.loan_app.repository.CustomerRepository;
import com.enigma.loan_app.repository.LoanTransactionRepository;
import com.enigma.loan_app.service.CustomerService;
import com.enigma.loan_app.service.LoanTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTransactionServiceImpl implements LoanTransactionService {

    private final LoanTransactionRepository loanTransactionRepository;
    private final LoanTypeServiceImpl loanTypeService;
    private final InstallmentTypeServiceImpl installmentTypeService;
    private final CustomerRepository customerRepository;

    @Override
    public LoanTransaction create(LoanTransactionRequest request) {
        String loanTypeId = request.getLoanType().getId();
        String installmentTypeId = request.getInstallmentType().getId();
        String customerId = request.getCustomer().getId();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (loanTypeId != null &&  installmentTypeId!= null && customer != null) {
            LoanTransaction loanTransaction = LoanTransaction.builder()
                    .loanType(loanTypeService.getById(loanTypeId))
                    .installmentType(installmentTypeService.getById(installmentTypeId))
                    .customer(customer)
                    .nominal(request.getNominal())
                    .approvedAt(null)
                    .approvedBy(null)
                    .approvalStatus(null)
                    .loanTransactionDetails(null)
                    .createdAt(Long.valueOf(String.valueOf(Math.round(Math.random() * (1699999 - 1600000 + 1) + 1600000))))
                    .updatedAt(null)
                    .build();
            return loanTransactionRepository.save(loanTransaction);
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoanTransaction approveRequest(ApproveTransactionRequest request) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(request.getLoanTransactionId()).orElse(null);
        if (loanTransaction != null) {

            List<LoanTransactionDetail> loanTransactionDetails = List.of(LoanTransactionDetail.builder()
                    .transactionDate(Long.valueOf(String.valueOf(Math.round(Math.random() * (1699999 - 1600000 + 1) + 1600000))))
                    .nominal(loanTransaction.getNominal())
                    .loanStatus(LoanStatus.UNPAID)
                    .createdAt(loanTransaction.getCreatedAt())
                    .updatedAt(Long.valueOf(String.valueOf(Math.round(Math.random() * (1699999 - 1600000 + 1) + 1600000))))
                    .build());

            LoanTransaction transaction =  LoanTransaction.builder()
                    .id(loanTransaction.getId())
                    .loanType(loanTransaction.getLoanType())
                    .installmentType(loanTransaction.getInstallmentType())
                    .customer(loanTransaction.getCustomer())
                    .nominal(loanTransaction.getNominal())
                    .updatedAt(Long.valueOf(String.valueOf(Math.round(Math.random() * (1699999 - 1600000 + 1) + 1600000))))
                    .approvedAt(Long.valueOf(String.valueOf(Math.round(Math.random() * (1699999 - 1600000 + 1) + 1600000))))
                    .approvedBy(SecurityContextHolder.getContext().getAuthentication().getName())
                    .approvalStatus(ApprovalStatus.APPROVED)
                    .createdAt(loanTransaction.getCreatedAt())
                    .updatedAt(loanTransaction.getUpdatedAt())
                    .loanTransactionDetails(loanTransactionDetails)
                    .build();
            for (LoanTransactionDetail i : loanTransactionDetails) i.setLoanTransaction(transaction);
            loanTransactionRepository.saveAndFlush(transaction);
            return transaction;
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public String payInstallment(String id) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(id).orElse(null);
        if (loanTransaction != null) {
            LoanStatus loanStatus = loanTransaction.getLoanTransactionDetails().get(0).getLoanStatus();
            if (loanStatus == LoanStatus.PAID) return null;
            Double paymentNominal = loanTransaction.getNominal() / loanTransaction.getInstallmentType().getInstallmentType().intValue;
            Double currentNominal = loanTransaction.getLoanTransactionDetails().get(0).getNominal();
            double newNominal = currentNominal - paymentNominal;
            if (currentNominal - paymentNominal <= 0) newNominal = 1;
            System.out.println(paymentNominal);
            LoanTransactionDetail loanTransactionDetail = LoanTransactionDetail.builder()
                    .id(loanTransaction.getLoanTransactionDetails().get(0).getId())
                    .transactionDate(Long.valueOf(String.valueOf(Math.round(Math.random() * (1699999 - 1600000 + 1) + 1600000))))
                    .nominal(newNominal)
                    .loanStatus(checkStatus(newNominal))
                    .createdAt(loanTransaction.getCreatedAt())
                    .updatedAt(Long.valueOf(String.valueOf(Math.round(Math.random() * (1699999 - 1600000 + 1) + 1600000))))
                    .loanTransaction(loanTransaction)
                    .build();

            LoanTransaction transaction =  LoanTransaction.builder()
                    .id(loanTransaction.getId())
                    .loanType(loanTransaction.getLoanType())
                    .installmentType(loanTransaction.getInstallmentType())
                    .customer(loanTransaction.getCustomer())
                    .nominal(loanTransaction.getNominal())
                    .approvedAt(loanTransaction.getApprovedAt())
                    .approvedBy(loanTransaction.getApprovedBy())
                    .approvalStatus(loanTransaction.getApprovalStatus())
                    .loanTransactionDetails(List.of(loanTransactionDetail))
                    .createdAt(loanTransactionDetail.getCreatedAt())
                    .updatedAt(loanTransactionDetail.getUpdatedAt())
                    .build();
            loanTransactionRepository.save(transaction);
            return String.format("LoanStatus: %s, Remaining: %s", loanTransactionDetail.getLoanStatus(), loanTransactionDetail.getNominal());
        }
        return null;
    }

    @Override
    public LoanTransaction getTransactionById(String id) {
        return loanTransactionRepository.findById(id).orElse(null);
    }

    @Override
    public LoanStatus checkStatus(Double nominal) {
        if (nominal <= 1) return LoanStatus.PAID;
        if (nominal > 1) return LoanStatus.UNPAID;
        return null;
    }
}
