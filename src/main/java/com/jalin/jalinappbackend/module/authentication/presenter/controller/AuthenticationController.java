package com.jalin.jalinappbackend.module.authentication.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User newUser = new User();
        newUser.setEmail(requestBody.getEmail());
        newUser.setPassword(requestBody.getPassword());

        UserDetails newUserDetails = new UserDetails();
        newUserDetails.setMobileNumber(requestBody.getMobileNumber());
        newUserDetails.setIdCardNumber(requestBody.getIdCardNumber());
        newUserDetails.setFullName(requestBody.getFullName());
        newUserDetails.setDateOfBirth(requestBody.getDateOfBirth());
        newUserDetails.setAddress(requestBody.getAddress());
        newUserDetails.setProvince(requestBody.getProvince());
        newUserDetails.setCity(requestBody.getCity());
        newUserDetails.setSubDistrict(requestBody.getSubDistrict());
        newUserDetails.setPostalCode(requestBody.getPostalCode());
        newUserDetails.setMaritalStatus(requestBody.getMaritalStatus());
        newUserDetails.setBankingGoals(requestBody.getBankingGoals());
        newUserDetails.setOccupation(requestBody.getBankingGoals());
        newUserDetails.setSourceOfIncome(requestBody.getSourceOfIncome());
        newUserDetails.setIncomeRange(requestBody.getIncomeRange());

        authenticationService.register(newUser, newUserDetails);
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
