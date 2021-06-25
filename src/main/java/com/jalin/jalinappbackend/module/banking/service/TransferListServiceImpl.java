package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.banking.entity.TransferList;
import com.jalin.jalinappbackend.module.banking.model.TransferListDto;
import com.jalin.jalinappbackend.module.banking.repository.TransferListRepository;
import com.jalin.jalinappbackend.module.banking.service.model.GetCustomerFullNameResponse;
import com.jalin.jalinappbackend.utility.FakerUtility;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TransferListServiceImpl implements TransferListService {
    @Value("${resource.server.url}")
    private String BASE_URL;
    private static final String FIND_CUSTOMER_ENDPOINT = "/api/v1/customers/find";
    private static final String FIND_CUSTOMER_ENDPOINT_ID_CARD_NUMBER_PARAMETER = "idCardNumber=";
    private static final String FIND_CUSTOMER_ENDPOINT_ACCOUNT_NUMBER_PARAMETER = "accountNumber=";
    @Autowired
    private FakerUtility fakerUtility;
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransferListRepository transferListRepository;

    @Override
    public TransferListDto addTransferList(String corporateId, String beneficiaryAccountNumber) {
        User user = getSignedInUser();
        if (!corporateId.equals("212")) {
            TransferList transferList = new TransferList();
            transferList.setCorporateId(corporateId);
            transferList.setAccountNumber(beneficiaryAccountNumber);
            transferList.setFullName(fakerUtility.initialize().name().fullName());
            transferList.setUser(user);
            return modelMapperUtility.initialize()
                    .map(transferListRepository.save(transferList), TransferListDto.class);
        } else {
            TransferList transferList = new TransferList();
            transferList.setCorporateId(corporateId);
            transferList.setAccountNumber(beneficiaryAccountNumber);
            transferList.setFullName(getCustomerFullName(beneficiaryAccountNumber));
            transferList.setUser(user);
            return modelMapperUtility.initialize()
                    .map(transferListRepository.save(transferList), TransferListDto.class);
        }
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String getCustomerFullName(String accountNumber) {
        ResponseEntity<GetCustomerFullNameResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + FIND_CUSTOMER_ENDPOINT + "?" +
                        FIND_CUSTOMER_ENDPOINT_ID_CARD_NUMBER_PARAMETER + "&" +
                        FIND_CUSTOMER_ENDPOINT_ACCOUNT_NUMBER_PARAMETER + accountNumber,
                GetCustomerFullNameResponse.class);
        return Objects.requireNonNull(response.getBody()).getFullName();
    }
}
