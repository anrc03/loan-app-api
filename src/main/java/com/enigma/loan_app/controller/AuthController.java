package com.enigma.loan_app.controller;

import com.enigma.loan_app.constant.AppPath;
import com.enigma.loan_app.dto.request.AuthRequest;
import com.enigma.loan_app.dto.request.LoginRequest;
import com.enigma.loan_app.dto.response.CommonResponse;
import com.enigma.loan_app.dto.response.CustomerResponse;
import com.enigma.loan_app.dto.response.LoginResponse;
import com.enigma.loan_app.dto.response.RegisterResponse;
import com.enigma.loan_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.AUTHENTICATION)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody AuthRequest authRequest) {
        System.out.println(authRequest);
        CustomerResponse response = authService.registerCustomer(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        CommonResponse.<CustomerResponse>builder()
                                .message("Successfully Registered")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        RegisterResponse response = authService.registerAdmin(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        CommonResponse.<RegisterResponse>builder()
                                .message("Successfully Registered as Admin")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonResponse.<LoginResponse>builder()
                                .message("Successfully Login")
                                .data(loginResponse)
                                .build()
                );
    }
}
