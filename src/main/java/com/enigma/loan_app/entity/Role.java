package com.enigma.loan_app.entity;

import com.enigma.loan_app.constant.ERole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String role_id;

    @Enumerated(EnumType.STRING)
    private ERole name;

}
