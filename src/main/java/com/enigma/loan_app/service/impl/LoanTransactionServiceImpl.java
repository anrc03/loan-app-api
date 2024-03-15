package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.constant.ApprovalStatus;
import com.enigma.loan_app.constant.LoanStatus;
import com.enigma.loan_app.dto.request.ApproveTransactionRequest;
import com.enigma.loan_app.dto.request.LoanTransactionRequest;
import com.enigma.loan_app.entity.Customer;
import com.enigma.loan_app.entity.LoanTransaction;
import com.enigma.loan_app.entity.LoanTransactionDetail;
import com.enigma.loan_app.repository.CustomerRepository;
import com.enigma.loan_app.repository.LoanTransactionRepository;
import com.enigma.loan_app.security.JwtUtil;
import com.enigma.loan_app.service.LoanTransactionService;
import com.enigma.loan_app.util.ValidationUtil;
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
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;

    @Override
    public LoanTransaction create(LoanTransactionRequest request) {
        String loanTypeId = request.getLoanType().getId();
        String installmentTypeId = request.getInstallmentType().getId();
        String customerId = request.getCustomer().getId();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (loanTypeId != null &&  installmentTypeId!= null && customer != null) {
            String[] dateArr =  String.valueOf(LocalDate.now()).split("-");
            Long currentDate = Long.valueOf(String.join("", dateArr));
            LoanTransaction loanTransaction = LoanTransaction.builder()
                    .loanType(loanTypeService.getById(loanTypeId))
                    .installmentType(installmentTypeService.getById(installmentTypeId))
                    .customer(customer)
                    .nominal(request.getNominal())
                    .interestRate(null)
                    .approvedAt(null)
                    .approvedBy(null)
                    .approvalStatus(null)
                    .loanTransactionDetails(null)
                    .createdAt(currentDate)
                    .updatedAt(null)
                    .build();
            return loanTransactionRepository.save(loanTransaction);
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoanTransaction approveOrRejectRequest(ApproveTransactionRequest request) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(request.getLoanTransactionId()).orElse(null);
        if (loanTransaction != null) {
            byte approvalStatus = request.getApprovalStatus();
            String[] dateArr =  String.valueOf(LocalDate.now()).split("-");
            Long currentDate = Long.valueOf(String.join("", dateArr));
            Double rate = Double.valueOf(request.getInterestRates()) / 100;
            Double interest = loanTransaction.getNominal() + (rate * loanTransaction.getNominal());

            List<LoanTransactionDetail> loanTransactionDetails = List.of(LoanTransactionDetail.builder()
                    .transactionDate(currentDate)
                    .nominal(interest)
                    .loanStatus(LoanStatus.UNPAID)
                    .updatedAt(currentDate)
                    .build());

            LoanTransaction transaction = LoanTransaction.builder()
                    .id(request.getLoanTransactionId())
                    .loanType(loanTransaction.getLoanType())
                    .installmentType(loanTransaction.getInstallmentType())
                    .customer(loanTransaction.getCustomer())
                    .nominal(loanTransaction.getNominal())
                    .interestRate(request.getInterestRates())
                    .approvedAt(currentDate)
                    .approvedBy(SecurityContextHolder.getContext().getAuthentication().getName())
                    .approvalStatus(ApprovalStatus.values()[approvalStatus])
                    .createdAt(loanTransaction.getCreatedAt())
                    .updatedAt(currentDate)
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
        if (loanTransaction != null && loanTransaction.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            String[] dateArr =  String.valueOf(LocalDate.now()).split("-");
            Long currentDate = Long.valueOf(String.join("", dateArr));
            LoanStatus loanStatus = loanTransaction.getLoanTransactionDetails().get(loanTransaction.getLoanTransactionDetails().size()-1).getLoanStatus();

            if (loanStatus == LoanStatus.PAID) return "PAID";

            Double interestValue = loanTransaction.getLoanTransactionDetails().get(0).getNominal();
            Double paymentNominal = interestValue / loanTransaction.getInstallmentType().getInstallmentType().intValue;
            Double debt = loanTransaction.getLoanTransactionDetails().get(loanTransaction.getLoanTransactionDetails().size()-1).getNominal();
            double newNominal = debt - paymentNominal;
            if (newNominal < 0) newNominal = 0;

            LoanTransactionDetail loanTransactionDetail = LoanTransactionDetail.builder()
                    .transactionDate(loanTransaction.getCreatedAt())
                    .nominal(newNominal)
                    .loanStatus(checkStatus(newNominal))
                    .updatedAt(currentDate)
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
                    .createdAt(loanTransaction.getCreatedAt())
                    .updatedAt(loanTransaction.getUpdatedAt())
                    .interestRate(loanTransaction.getInterestRate())
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
        if (nominal <= 0) return LoanStatus.PAID;
        if (nominal > 0) return LoanStatus.UNPAID;
        return null;
    }

    @Override
    public List<LoanTransaction> getAllTransactionByToken() {
        String token = validationUtil.extractTokenFromHeader();
        String customerId= jwtUtil.getUserInfoByToken(token).get("customerId");
        System.out.println("Token info: " + jwtUtil.getUserInfoByToken(token));
        System.out.println("this is token: " + token);
        System.out.println("this is customerId: " + customerId);
        return loanTransactionRepository.findAllByCustomerId(customerId);
    }
}
