package com.enigma.loan_app.dto.response;

import com.enigma.loan_app.constant.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanTransactionDetailResponse {
    private String id;
    private Long transactionDate;
    private Double nominal;
    private LoanStatus loanStatus;
    private Long updatedAt;
}
