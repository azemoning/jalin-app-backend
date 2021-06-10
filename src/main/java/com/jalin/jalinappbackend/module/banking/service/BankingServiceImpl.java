package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.banking.service.model.GetBankAccountResponse;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BankingServiceImpl implements BankingService {
    @Value("${resource.server.url}")
    private String BASE_URL;
    private static final String GET_BANK_ACCOUNT_ENDPOINT = "/api/v1/accounts/";
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public BigDecimal getAccountBalance() {
        UserDetails userDetails = getUserDetails();
        ResponseEntity<GetBankAccountResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_BANK_ACCOUNT_ENDPOINT + userDetails.getAccountNumber(),
                GetBankAccountResponse.class);
        return Objects.requireNonNull(response.getBody()).getBalance();
    }

    private UserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userDetailsRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));
    }
}
