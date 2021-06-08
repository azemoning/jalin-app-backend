package com.jalin.jalinappbackend.module.authentication.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetailsImpl;
import com.jalin.jalinappbackend.module.authentication.jwt.JwtTokenUtility;
import com.jalin.jalinappbackend.module.authentication.presenter.model.LoginRequest;
import com.jalin.jalinappbackend.module.authentication.presenter.model.RegisterRequest;
import com.jalin.jalinappbackend.module.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Autowired
    private JwtTokenUtility jwtTokenUtility;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest requestBody) {
        authenticationService.register(
                requestBody.getMobileNumber(),
                new User(requestBody.getEmail(), requestBody.getPassword()));
        return new ResponseEntity<>(
                new SuccessResponse(true, "User registered successfully"),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest requestBody) {
        authenticationService.login(requestBody.getEmail(), requestBody.getPassword());
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenUtility.generateToken(userDetails))
                .body(new SuccessResponse(true, "Login successful"));
    }
}
