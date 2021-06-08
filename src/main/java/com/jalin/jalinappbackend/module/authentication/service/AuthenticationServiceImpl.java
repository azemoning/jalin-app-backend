package com.jalin.jalinappbackend.module.authentication.service;

import com.jalin.jalinappbackend.exception.AuthenticateFailedException;
import com.jalin.jalinappbackend.exception.RegisterFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.Role;
import com.jalin.jalinappbackend.module.authentication.entity.RoleEnum;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.RoleRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.authentication.service.model.FindCustomerByMobileNumberResponse;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String BASE_URL = "https://jalin-bank-resource-server.herokuapp.com";
    private static final String FIND_CUSTOMER_BY_MOBILE_PHONE_ENDPOINT = "/api/v1/customers/find?mobileNumber=";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public void register(String mobileNumberRequestBody, User userRequestBody) {
        if (userDetailsRepository.findByMobileNumber(mobileNumberRequestBody).isPresent()) {
            throw new RegisterFailedException("Mobile number already registered as Jalin App user");
        }

        try {
            ResponseEntity<FindCustomerByMobileNumberResponse> response = restTemplateUtility.initialize()
                    .getForEntity(
                            BASE_URL + FIND_CUSTOMER_BY_MOBILE_PHONE_ENDPOINT + mobileNumberRequestBody,
                            FindCustomerByMobileNumberResponse.class);

            Role userRole = roleRepository.findByRoleName(RoleEnum.USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

            User user = userRepository.save(new User(
                    userRequestBody.getEmail(),
                    passwordEncoder.encode(userRequestBody.getPassword()),
                    userRole));

            UserDetails userDetails = new UserDetails();
            userDetails.setFullName(response.getBody().getFullName());
            userDetails.setAccountNumber(response.getBody().getAccountNumber());
            userDetails.setMobileNumber(response.getBody().getMobileNumber());
            userDetails.setUser(user);
            userDetailsRepository.save(userDetails);
        } catch (HttpClientErrorException exception) {
            throw new RegisterFailedException("Mobile number not registered as Jalin Bank customer");
        }
    }

    @Override
    public void login(String email, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException exception) {
            throw new AuthenticateFailedException("Incorrect email or password");
        }
    }
}
