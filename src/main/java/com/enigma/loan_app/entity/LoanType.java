package com.enigma.loan_app.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "t_loan_type")
public class LoanType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "loan_type_id")
    private String id;

    @Column(nullable = false)
    private String type;

    @Column(name = "maximum_loan", nullable = false, columnDefinition = "DOUBLE PRECISION CHECK  (maximum_loan > 0.0)")
    private Double maxLoan;
}
