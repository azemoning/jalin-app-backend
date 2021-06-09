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
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewBankAccountRequest;
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewBankAccountResponse;
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewCustomerRequest;
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewCustomerResponse;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String BASE_URL = "https://jalin-bank-resource-server.herokuapp.com";
    private static final String ADD_NEW_CUSTOMER_ENDPOINT = "/api/v1/customers";
    private static final String ADD_NEW_BANK_ACCOUNT_ENDPOINT = "/api/v1/accounts?customerId=";
    private static final String FIND_CUSTOMER_BY_ID_CARD_NUMBER_ENDPOINT = "/api/v1/customers/find?idCardNumber=";
    private static final String IDR_CURRENCY = "IDR";
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public void register(User userRequestBody, UserDetails userDetailsRequestBody) {
        if (userDetailsRepository.findByMobileNumber(userDetailsRequestBody.getMobileNumber()).isPresent() ||
                userRepository.findByEmail(userRequestBody.getEmail()).isPresent()) {
            throw new RegisterFailedException("Email address or mobile number already registered");
        } else if (isIdCardRegisteredAtServer(userDetailsRequestBody.getIdCardNumber()) ||
                userDetailsRepository.findByIdCardNumber(userDetailsRequestBody.getIdCardNumber()).isPresent()) {
            throw new RegisterFailedException("ID card already registered");
        }

        ResponseEntity<AddNewCustomerResponse> addNewCustomerResponse = addCustomer(
                userDetailsRequestBody.getIdCardNumber(),
                userDetailsRequestBody.getFullName());

        ResponseEntity<AddNewBankAccountResponse> addNewBankAccountResponse = addBankAccount(
                addNewCustomerResponse.getBody().getCustomerId());

        Role userRole = roleRepository.findByRoleName(RoleEnum.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User newUser = userRepository.save(
                new User(
                        userRequestBody.getEmail(),
                        passwordEncoder.encode(userRequestBody.getPassword()),
                        userRole));

        UserDetails newUserDetails = modelMapperUtility.initialize()
                .map(userDetailsRequestBody, UserDetails.class);
        newUserDetails.setAccountNumber(addNewBankAccountResponse.getBody().getAccountNumber());
        newUserDetails.setJalinId(newUserDetails.getFullName() + "-" + newUserDetails.getAccountNumber());
        newUserDetails.setUser(newUser);
        userDetailsRepository.save(newUserDetails);
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

    private Boolean isIdCardRegisteredAtServer(String idCardNumber) {
        try {
            restTemplateUtility.initialize().getForObject(
                    BASE_URL + FIND_CUSTOMER_BY_ID_CARD_NUMBER_ENDPOINT + idCardNumber,
                    Object.class);
        } catch (HttpClientErrorException exception) {
            return false;
        }
        return true;
    }

    private ResponseEntity<AddNewCustomerResponse> addCustomer(String idCardNumber, String fullName) {
        AddNewCustomerRequest request = new AddNewCustomerRequest();
        request.setIdCardNumber(idCardNumber);
        request.setFullName(fullName);

        HttpEntity<AddNewCustomerRequest> requestBody = new HttpEntity<>(request);
        return restTemplateUtility.initialize().postForEntity(
                BASE_URL + ADD_NEW_CUSTOMER_ENDPOINT,
                requestBody,
                AddNewCustomerResponse.class);
    }

    private ResponseEntity<AddNewBankAccountResponse> addBankAccount(String customerId) {
        AddNewBankAccountRequest request = new AddNewBankAccountRequest();
        request.setCurrency(IDR_CURRENCY);
        request.setBalance(new BigDecimal(0));

        HttpEntity<AddNewBankAccountRequest> requestBody = new HttpEntity<>(request);
        return restTemplateUtility.initialize().postForEntity(
                BASE_URL + ADD_NEW_BANK_ACCOUNT_ENDPOINT + customerId ,
                requestBody,
                AddNewBankAccountResponse.class);
    }
}
