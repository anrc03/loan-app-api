package com.enigma.loan_app.entity;

import com.enigma.loan_app.constant.ApprovalStatus;
import com.enigma.loan_app.constant.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "trx_loan")
public class LoanTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    private LoanType loanType;

    @ManyToOne
    @JoinColumn(name = "installment_type_id")
    private InstallmentType installmentType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "nominal", columnDefinition = "DOUBLE PRECISION CHECK (nominal > 0)")
    private Double nominal;

    @Column(name = "interest_rate", columnDefinition = "INT CHECK (interest_rate > 0)")
    private Integer interestRate;

    @Column(name = "approved_at")
    private Long approvedAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status")
    private LoanStatus loanStatus;

    @OneToMany(mappedBy = "loanTransaction", cascade = CascadeType.MERGE)
    private List<LoanTransactionDetail> loanTransactionDetails;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

}
