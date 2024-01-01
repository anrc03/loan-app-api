package com.enigma.loan_app.dto.request;

import com.enigma.loan_app.entity.Customer;
import com.enigma.loan_app.entity.InstallmentType;
import com.enigma.loan_app.entity.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanTransactionRequest {
    private LoanType loanType;
    private InstallmentType installmentType;
    private Customer customer;
    private Double nominal;
}
