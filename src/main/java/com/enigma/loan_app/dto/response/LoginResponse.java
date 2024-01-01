package com.enigma.loan_app.dto.response;

import com.enigma.loan_app.constant.ERole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoginResponse {
    private String email;
    private List<ERole> roles;
    private String token;
}
