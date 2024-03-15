package com.enigma.loan_app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.loan_app.entity.AppUser;
import com.enigma.loan_app.entity.Customer;
import com.enigma.loan_app.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtil {
    @Value("${app.loan_app.jwt.jwt-secret}")
    private String jwtSecret;

    @Value("${app.loan_app.jwt.app-name}")
    private String appName;

    @Value("${app.loan_app.jwt.jwtExpirationInSecond}")
    private Integer jwtExpirationInSecond;

    private final CustomerService customerService;

    public JwtUtil(CustomerService customerService) {
        this.customerService = customerService;
    }

    public String generateToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Optional<Customer> customer = customerService.getCustomerByUserId(appUser.getUserId());
            String customerId = customer.map(Customer::getId).orElse("");
            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(appUser.getUserId())
                    .withClaim("customerId", customerId)
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSecond))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", appUser.getRoles().stream().map(Enum::name).toList())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        } catch (JWTCreationException e) {
            return false;
        }
    }

    public Map<String, String> getUserInfoByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("userId", decodedJWT.getSubject());
            userInfo.put("customerId", decodedJWT.getClaim("customerId").asString());
            return userInfo;
        } catch (JWTVerificationException e) {
            throw new RuntimeException();
        }
    }
}
