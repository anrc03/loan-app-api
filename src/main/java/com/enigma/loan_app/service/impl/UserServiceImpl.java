package com.enigma.loan_app.service.impl;

import com.enigma.loan_app.dto.request.AuthRequest;
import com.enigma.loan_app.dto.response.UserResponse;
import com.enigma.loan_app.entity.AppUser;
import com.enigma.loan_app.entity.Role;
import com.enigma.loan_app.entity.User;
import com.enigma.loan_app.repository.UserRepository;
import com.enigma.loan_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public AppUser loadUserByUserId(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));
        return AppUser.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));
        return AppUser.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    @Override
    public UserResponse getUserById(String id) {
        AppUser user = loadUserByUserId(id);
        return UserResponse.builder()
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    public User updateUser(User user) {
        if (user.getId() != null && loadUserByUserId(user.getId()) != null) {
            return userRepository.save(user);
        }
        return null;
    }

}
