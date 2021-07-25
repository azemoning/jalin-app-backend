package com.jalin.jalinappbackend.module.authentication.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetailsImpl;
import com.jalin.jalinappbackend.module.authentication.jwt.JwtTokenUtility;
import com.jalin.jalinappbackend.module.authentication.model.LoginAdminDto;
import com.jalin.jalinappbackend.module.authentication.model.LoginDto;
import com.jalin.jalinappbackend.module.authentication.presenter.model.LoginRequest;
import com.jalin.jalinappbackend.module.authentication.presenter.model.RegisterRequest;
import com.jalin.jalinappbackend.module.authentication.service.AuthenticationService;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${url.map.api}")
public class AuthenticationController {
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private JwtTokenUtility jwtTokenUtility;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> register(@Valid @ModelAttribute RegisterRequest requestBody) {
        User newUser = modelMapperUtility.initialize()
                .map(requestBody, User.class);
        UserDetails newUserDetails = modelMapperUtility.initialize()
                .map(requestBody, UserDetails.class);
        authenticationService.register(newUser, newUserDetails);
        return new ResponseEntity<>(
                new SuccessResponse(true, "User successfully registered"),
                HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> login(@Valid @ModelAttribute LoginRequest requestBody) {
        LoginDto loginDto = authenticationService.login(requestBody.getEmail(), requestBody.getPassword());
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwtTokenUtility.generateToken(userDetails);
        loginDto.setToken(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(new SuccessDetailsResponse(true, "Login successful", loginDto));
    }

    @PostMapping(
            path = "/admin/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> loginAdmin(@Valid @ModelAttribute LoginRequest requestBody) {
        LoginAdminDto loginAdminDto = authenticationService.loginAdmin(requestBody.getEmail(), requestBody.getPassword());
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwtTokenUtility.generateToken(userDetails);
        loginAdminDto.setToken(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(new SuccessDetailsResponse(true, "Login successful", loginAdminDto));
    }
}
