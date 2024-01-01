package com.enigma.loan_app.controller;

import com.enigma.loan_app.constant.AppPath;
import com.enigma.loan_app.dto.response.CommonResponse;
import com.enigma.loan_app.entity.InstallmentType;
import com.enigma.loan_app.service.impl.InstallmentTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.INSTALLMENT_TYPE)
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
@RequiredArgsConstructor
public class InstallmentTypeController {

    private final InstallmentTypeServiceImpl installmentTypeService;

    @PostMapping
    public ResponseEntity<?> createInstallmentType(@RequestBody InstallmentType type) {
        InstallmentType installmentType = installmentTypeService.create(type);
        if (installmentType != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            CommonResponse.<InstallmentType>builder()
                                    .message("Successfully added new Installment Type")
                                    .data(installmentType)
                                    .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                        CommonResponse.<InstallmentType>builder()
                                .message("Not a valid installment type")
                                .build()
                );
    }


    @GetMapping
    public ResponseEntity<?> getAllInstallmentType() {
        List<InstallmentType> installmentType = installmentTypeService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.<List<InstallmentType>>builder()
                        .message("Fetch Success")
                        .data(installmentType)
                        .build()
        );

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getInstallmentTypeById(@PathVariable String id) {
        InstallmentType installmentType = installmentTypeService.getById(id);
        if (installmentType != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    CommonResponse.<InstallmentType>builder()
                            .message("Fetch Success")
                            .data(installmentType)
                            .build()
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                CommonResponse.<InstallmentType>builder()
                        .message("Id Not Found")
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<?> updateInstallmentType(@RequestBody InstallmentType type) {
        InstallmentType installmentType = installmentTypeService.update(type);
        if (type.getId() != null && installmentType != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            CommonResponse.<InstallmentType>builder()
                                    .message("Update Success")
                                    .data(installmentType)
                                    .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        CommonResponse.<InstallmentType>builder()
                                .message("Not Found")
                                .build()
                );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteInstallmentType(@PathVariable String id) {
        installmentTypeService.delete(id);
        return ResponseEntity.ok("Successfully Deleted");
    }

}
