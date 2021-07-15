package com.jalin.jalinappbackend.module.authentication.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.model.UserDetailsDto;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.banking.service.BankingService;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private BankingService bankingService;

    @Override
    public UserDetailsDto getUserDetails() {
        User signedInUser = getSignedInUser();
        UserDetails userDetails = userDetailsRepository.findByUser(signedInUser)
                .orElseThrow(() -> new ResourceNotFoundException("User's details not found"));

        UserDetailsDto userDetailsDto = modelMapperUtility.initialize()
                .map(userDetails, UserDetailsDto.class);
        userDetailsDto.setEmail(signedInUser.getEmail());
        userDetailsDto.setBalance(getAccountBalance());

        return userDetailsDto;
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String getAccountBalance() {
        return bankingService.getAccountBalance().getBalance();
    }
}
