package com.enigma.loan_app.dto.response;

import com.enigma.loan_app.constant.CustomerStatus;
import com.enigma.loan_app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerResponse {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;
    private CustomerStatus status;
    private UserResponse user;
}
