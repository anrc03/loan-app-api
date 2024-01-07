package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.constant.CustomerStatus;
import com.enigma.loan_app.constant.ERole;
import com.enigma.loan_app.dto.request.AuthRequest;
import com.enigma.loan_app.dto.request.LoginRequest;
import com.enigma.loan_app.dto.response.*;
import com.enigma.loan_app.entity.AppUser;
import com.enigma.loan_app.entity.Customer;
import com.enigma.loan_app.entity.Role;
import com.enigma.loan_app.entity.User;
import com.enigma.loan_app.repository.UserRepository;
import com.enigma.loan_app.security.JwtUtil;
import com.enigma.loan_app.service.AuthService;
import com.enigma.loan_app.service.CustomerService;
import com.enigma.loan_app.service.RoleService;
import com.enigma.loan_app.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerService customerService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public CustomerResponse registerCustomer(AuthRequest authRequest) {
        try {
            Role role = Role.builder()
                    .name(ERole.ROLE_CUSTOMER)
                    .build();
            role = roleService.getOrSave(role);

            User user = User.builder()
                    .email(authRequest.getEmail())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .roles(List.of(role))
                    .build();
            userRepository.saveAndFlush(user);

            Customer customer = Customer.builder()
                    .firstName(authRequest.getFirstName())
                    .lastName(authRequest.getLastName())
                    .dateOfBirth(LocalDate.parse(authRequest.getDateOfBirth()))
                    .phone(authRequest.getPhone())
                    .status(CustomerStatus.ACTIVE)
                    .user(user)
                    .build();
            CustomerResponse customerResponse = customerService.create(customer);

            return CustomerResponse.builder()
                    .id(customerResponse.getId())
                    .firstName(customerResponse.getFirstName())
                    .lastName(customerResponse.getLastName())
                    .dateOfBirth(customerResponse.getDateOfBirth())
                    .phone(customerResponse.getPhone())
                    .status(customerResponse.getStatus())
                    .user(UserResponse.builder()
                            .email(user.getEmail())
                            .roles(user.getRoles().stream().map(Role::getName).toList())
                            .build())
                    .build();

        }
        catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }
    }


    @Override
    public LoginResponse login(LoginRequest request) {
        validationUtil.validate(request);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail().toLowerCase(),
                request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = (AppUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);
        return LoginResponse.builder()
                .token(token)
                .email(appUser.getUsername())
                .roles(appUser.getRoles())
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest authRequest) {
        try {
            Role admin = Role.builder().name(ERole.ROLE_ADMIN).build();
            Role staff = Role.builder().name(ERole.ROLE_STAFF).build();
            admin = roleService.getOrSave(admin);
            staff = roleService.getOrSave(staff);

            User user = User.builder()
                    .email(authRequest.getEmail())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .roles(List.of(admin, staff))
                    .build();
            userRepository.saveAndFlush(user);

            return RegisterResponse.builder()
                    .email(authRequest.getEmail())
                        .roles(user.getRoles().stream().map(Role::getName).toList())
                    .build();
        }
        catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }
    }
}
