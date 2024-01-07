package com.enigma.loan_app.controller;

import com.enigma.loan_app.constant.AppPath;
import com.enigma.loan_app.dto.response.CommonResponse;
import com.enigma.loan_app.dto.response.LoginResponse;
import com.enigma.loan_app.dto.response.UserResponse;
import com.enigma.loan_app.entity.User;
import com.enigma.loan_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.USER)
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonResponse.<UserResponse>builder()
                                .message("Fetch Success")
                                .data(response)
                                .build()
                );
    }
}
