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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(value = "/{id}")
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
                        CommonResponse.<CustomerResponse>builder()
                                .message("Not Found")
                                .build()
                );
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        customerService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonResponse.<Customer>builder()
                                .message("Successfully Deleted")
                                .build()
                );
    }
}
