package com.enigma.loan_app.entity;

import com.enigma.loan_app.constant.LoanStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "trx_loan_detail")
public class LoanTransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "transaction_date")
    private Long transactionDate;

    @Column(name = "nominal", columnDefinition = "DOUBLE PRECISION CHECK (nominal >= 0)")
    private Double nominal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trx_loan_id")
    private LoanTransaction loanTransaction;

    @Column(name = "at_installment_number")
    private Integer atInstallmentNumber;

    @Column(name = "updated_at")
    private Long updatedAt;

}
