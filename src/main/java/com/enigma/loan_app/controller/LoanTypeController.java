package com.enigma.loan_app.controller;

import com.enigma.loan_app.constant.AppPath;
import com.enigma.loan_app.dto.response.CommonResponse;
import com.enigma.loan_app.entity.LoanType;
import com.enigma.loan_app.service.impl.LoanTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.LOAN_TYPE)
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
@RequiredArgsConstructor
public class LoanTypeController {

    private final LoanTypeServiceImpl loanTypeService;

    @PostMapping
    public ResponseEntity<?> createLoanType(@RequestBody LoanType type) {
        LoanType loanType = loanTypeService.create(type);
        if (loanType != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            CommonResponse.<LoanType>builder()
                                    .message("Successfully added new Loan Type")
                                    .data(loanType)
                                    .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                        CommonResponse.<LoanType>builder()
                                .message("Cannot be null")
                                .build()
                );
    }


    @GetMapping
    public ResponseEntity<?> getAllLoanType() {
        List<LoanType> loanTypes = loanTypeService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.<List<LoanType>>builder()
                        .message("Fetch Success")
                        .data(loanTypes)
                        .build()
        );

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getLoanTypeById(@PathVariable String id) {
        LoanType loanType = loanTypeService.getById(id);
        if (loanType != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<LoanType>builder()
                            .message("Fetch Success")
                            .data(loanType)
                            .build()
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                CommonResponse.<LoanType>builder()
                        .message("Id Not Found")
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<?> updateLoanType(@RequestBody LoanType type) {
        LoanType loanType = loanTypeService.update(type);
        if (type.getId() != null && loanType != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            CommonResponse.<LoanType>builder()
                                    .message("Update Success")
                                    .data(loanType)
                                    .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        CommonResponse.<LoanType>builder()
                                .message("Not Found")
                                .build()
                );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteLoanType(@PathVariable String id) {
        loanTypeService.delete(id);
        return ResponseEntity.ok("Successfully Deleted");
    }
}
