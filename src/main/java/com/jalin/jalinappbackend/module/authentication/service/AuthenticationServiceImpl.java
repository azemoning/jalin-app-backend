package com.jalin.jalinappbackend.module.authentication.service;

import com.jalin.jalinappbackend.exception.AuthenticateFailedException;
import com.jalin.jalinappbackend.exception.ForbiddenException;
import com.jalin.jalinappbackend.exception.RegisterFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.Role;
import com.jalin.jalinappbackend.module.authentication.entity.RoleEnum;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.model.LoginAdminDto;
import com.jalin.jalinappbackend.module.authentication.model.LoginDto;
import com.jalin.jalinappbackend.module.authentication.repository.RoleRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewBankAccountRequest;
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewBankAccountResponse;
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewCustomerRequest;
import com.jalin.jalinappbackend.module.authentication.service.model.AddNewCustomerResponse;
import com.jalin.jalinappbackend.module.gamification.checkin.service.CheckInService;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
import com.jalin.jalinappbackend.module.gamification.point.service.PointService;
import com.jalin.jalinappbackend.utility.FakerUtility;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import com.jalin.jalinappbackend.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Value("${resource.server.url}")
    private String BASE_URL;

    private static final String ADD_NEW_CUSTOMER_ENDPOINT = "/v1/customers";
    private static final String ADD_NEW_BANK_ACCOUNT_ENDPOINT = "/v1/accounts?customerId=";
    private static final String FIND_CUSTOMER_ENDPOINT = "/v1/customers/find";
    private static final String FIND_CUSTOMER_ENDPOINT_ID_CARD_NUMBER_PARAMETER = "idCardNumber=";
    private static final String FIND_CUSTOMER_ENDPOINT_ACCOUNT_NUMBER_PARAMETER = "accountNumber=";
    private static final String IDR_CURRENCY = "IDR";
    private static final Integer INITIAL_BALANCE = 1000000000;
    @Autowired
    private FakerUtility fakerUtility;
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserUtility userUtility;
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
    @Autowired
    private PointService pointService;
    @Autowired
    private CheckInService checkInService;
    @Autowired
    private UserMissionService userMissionService;

    @Override
    @Transactional
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
                Objects.requireNonNull(addNewCustomerResponse.getBody()).getCustomerId());

        Role userRole = roleRepository.findByRoleName(RoleEnum.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User newUser = userRepository.save(
                new User(
                        userRequestBody.getEmail(),
                        passwordEncoder.encode(userRequestBody.getPassword()),
                        userRole));

        UserDetails newUserDetails = modelMapperUtility.initialize()
                .map(userDetailsRequestBody, UserDetails.class);
        newUserDetails.setAccountNumber(Objects.requireNonNull(addNewBankAccountResponse.getBody()).getAccountNumber());
        newUserDetails.setJalinId(initializeJalinId(newUserDetails.getFullName()));
        newUserDetails.setUser(newUser);
        userDetailsRepository.save(newUserDetails);

        pointService.initiateUserPoint(newUser);
        checkInService.initiateCheckInCounter(newUser);
        userMissionService.initiateUserMission(newUser);
    }

    @Override
    public LoginDto login(String email, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole().getRoleName().name().equals(RoleEnum.USER.name())) {
                UserDetails userDetails = userDetailsRepository.findByUser(user)
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                LoginDto loginDto = new LoginDto();
                loginDto.setFullName(userDetails.getFullName());
                loginDto.setEmail(user.getEmail());
                loginDto.setRole(user.getRole().getRoleName().name());
                return loginDto;
            } else {
                throw new ForbiddenException("Forbidden");
            }
        } catch (BadCredentialsException exception) {
            throw new AuthenticateFailedException("Incorrect email or password");
        }
    }

    @Override
    public LoginAdminDto loginAdmin(String email, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole().getRoleName().name().equals(RoleEnum.ADMIN.name())) {
                LoginAdminDto loginAdminDto = new LoginAdminDto();
                loginAdminDto.setEmail(user.getEmail());
                loginAdminDto.setRole(user.getRole().getRoleName().name());
                return loginAdminDto;
            } else {
                throw new ForbiddenException("Forbidden");
            }
        } catch (BadCredentialsException exception) {
            throw new AuthenticateFailedException("Incorrect email or password");
        }
    }

    private Boolean isIdCardRegisteredAtServer(String idCardNumber) {
        try {
            restTemplateUtility.initialize().getForObject(
                    BASE_URL + FIND_CUSTOMER_ENDPOINT + "?" +
                            FIND_CUSTOMER_ENDPOINT_ID_CARD_NUMBER_PARAMETER + idCardNumber + "&" +
                            FIND_CUSTOMER_ENDPOINT_ACCOUNT_NUMBER_PARAMETER,
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
        request.setBalance(new BigDecimal(INITIAL_BALANCE));

        HttpEntity<AddNewBankAccountRequest> requestBody = new HttpEntity<>(request);
        return restTemplateUtility.initialize().postForEntity(
                BASE_URL + ADD_NEW_BANK_ACCOUNT_ENDPOINT + customerId ,
                requestBody,
                AddNewBankAccountResponse.class);
    }

    private String initializeJalinId(String fullName) {
        String faker = fakerUtility.initialize().bothify("??????##");
        String name = fullName.toLowerCase(Locale.ROOT).replace(" ", ".");
        return (name + "." + faker);
    }
}
