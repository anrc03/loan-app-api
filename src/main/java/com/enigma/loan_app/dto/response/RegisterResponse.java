package com.enigma.loan_app.dto.response;

import com.enigma.loan_app.constant.ERole;
import com.enigma.loan_app.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RegisterResponse {
    private String email;
    private List<ERole> roles;
}
