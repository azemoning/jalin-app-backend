package com.jalin.jalinappbackend.module.authentication.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.presenter.model.RegisterRequest;
import com.jalin.jalinappbackend.module.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
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
}
