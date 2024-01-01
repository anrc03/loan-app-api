package com.enigma.loan_app.dto.response;

import com.enigma.loan_app.constant.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RoleResponse {
    private ERole name;
}
