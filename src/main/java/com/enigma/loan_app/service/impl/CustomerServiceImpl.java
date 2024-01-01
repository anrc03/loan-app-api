package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.constant.CustomerStatus;
import com.enigma.loan_app.dto.request.AuthRequest;
import com.enigma.loan_app.dto.response.CustomerResponse;
import com.enigma.loan_app.dto.response.UserResponse;
import com.enigma.loan_app.entity.Customer;
import com.enigma.loan_app.entity.Role;
import com.enigma.loan_app.entity.User;
import com.enigma.loan_app.repository.CustomerRepository;
import com.enigma.loan_app.service.CustomerService;
import com.enigma.loan_app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponse create(Customer c) {
        Customer customer = Customer.builder()
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .dateOfBirth(c.getDateOfBirth())
                .phone(c.getPhone())
                .status(c.getStatus())
                .user(c.getUser())
                .build();
        customerRepository.saveAndFlush(customer);
        return CustomerResponse.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .build();
    }

    @Override
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getStatus() == CustomerStatus.ACTIVE)
                .map(customer -> CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .user(UserResponse.builder()
                        .email(customer.getUser().getEmail())
                        .roles(customer.getUser().getRoles().stream().map(Role::getName).toList())
                        .build())
                .build())
                .toList();
    }

    @Override
    public CustomerResponse getById(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null && customer.getStatus() == CustomerStatus.ACTIVE) {
            return CustomerResponse.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .dateOfBirth(customer.getDateOfBirth())
                    .phone(customer.getPhone())
                    .status(customer.getStatus())
                    .user(UserResponse.builder()
                            .email(customer.getUser().getEmail())
                            .roles(customer.getUser().getRoles().stream().map(Role::getName).toList())
                            .build())
                    .build();
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public CustomerResponse update(AuthRequest authRequest) {
        Customer checkCustomer = customerRepository.findById(authRequest.getId()).orElse(null);
        if (checkCustomer != null) {
            String userId = checkCustomer.getUser().getId();
            User user = userService.updateUser(User.builder()
                            .id(userId)
                            .email(authRequest.getEmail())
                            .password(passwordEncoder.encode(authRequest.getPassword()))
                            .roles(checkCustomer.getUser().getRoles())
                    .build());
            Customer customer = Customer.builder()
                    .id(authRequest.getId())
                    .firstName(authRequest.getFirstName())
                    .lastName(authRequest.getLastName())
                    .dateOfBirth(LocalDate.parse(authRequest.getDateOfBirth()))
                    .phone(authRequest.getPhone())
                    .status(checkCustomer.getStatus())
                    .user(user)
                    .build();
            customerRepository.save(customer);
            return CustomerResponse.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .dateOfBirth(customer.getDateOfBirth())
                    .phone(customer.getPhone())
                    .status(customer.getStatus())
                    .user(UserResponse.builder()
                            .email(user.getEmail())
                            .roles(user.getRoles().stream().map(Role::getName).toList())
                            .build())
                    .build();
        }
        return null;

    }

    @Override
    public void delete(String id) {
        Customer checkCustomer = customerRepository.findById(id).orElse(null);
        if (checkCustomer != null) {
            Customer customer = Customer.builder()
                    .id(checkCustomer.getId())
                    .firstName(checkCustomer.getFirstName())
                    .lastName(checkCustomer.getLastName())
                    .dateOfBirth(checkCustomer.getDateOfBirth())
                    .phone(checkCustomer.getPhone())
                    .status(CustomerStatus.INACTIVE)
                    .user(checkCustomer.getUser())
                    .build();
            customerRepository.save(customer);
        }
    }
}
