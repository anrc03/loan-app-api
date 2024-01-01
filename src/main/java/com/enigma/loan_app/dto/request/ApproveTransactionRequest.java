package com.enigma.loan_app.dto.request;

import com.enigma.loan_app.entity.LoanTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApproveTransactionRequest {
    private String loanTransactionId;
    private Integer interestRates;
}
