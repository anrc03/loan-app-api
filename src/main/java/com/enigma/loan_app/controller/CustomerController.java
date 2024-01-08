package com.enigma.loan_app.controller;

import com.enigma.loan_app.constant.AppPath;
import com.enigma.loan_app.dto.request.AuthRequest;
import com.enigma.loan_app.dto.response.CommonResponse;
import com.enigma.loan_app.dto.response.CustomerResponse;
import com.enigma.loan_app.entity.Customer;
import com.enigma.loan_app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        CustomerResponse response = customerService.getById(id);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            CommonResponse.<CustomerResponse>builder()
                                    .message("Fetch Success")
                                    .data(response)
                                    .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        CommonResponse.<CustomerResponse>builder()
                                .message("Id Not Found")
                                .build()
                );

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> getAllCustomer() {
        List<CustomerResponse> response = customerService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonResponse.<List<CustomerResponse>>builder()
                                .message("Fetch Success")
                                .data(response)
                                .build()
                );
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> updateCustomer(@RequestBody AuthRequest request) {
        CustomerResponse customerResponse = customerService.update(request);
        if (request.getId() != null && customerResponse != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            CommonResponse.<CustomerResponse>builder()
                                    .message("Update Success")
                                    .data(customerResponse)
                                    .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        CommonResponse.<String>builder()
                                .message("Operation Failed")
                                .build()
                );
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        Boolean status = customerService.delete(id);
        if (status) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            CommonResponse.<String>builder()
                                    .message("Successfully Deleted")
                                    .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonResponse.<String>builder()
                                .message("Operation Failed")
                                .build()
                );
    }
}
