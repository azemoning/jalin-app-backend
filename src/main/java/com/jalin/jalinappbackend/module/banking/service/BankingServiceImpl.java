package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.exception.TransferFailedException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.service.model.FundTransferRequest;
import com.jalin.jalinappbackend.module.banking.service.model.FundTransferResponse;
import com.jalin.jalinappbackend.module.banking.service.model.GetBankAccountResponse;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class BankingServiceImpl implements BankingService {
    @Value("${resource.server.url}")
    private String BASE_URL;
    private static final String GET_BANK_ACCOUNT_ENDPOINT = "/api/v1/accounts/";
    private static final String FUND_TRANSFER_ENDPOINT = "/api/v1/transfers";
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public BigDecimal getAccountBalance() {
        UserDetails userDetails = getSignedInUserDetails();
        ResponseEntity<GetBankAccountResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_BANK_ACCOUNT_ENDPOINT + userDetails.getAccountNumber(),
                GetBankAccountResponse.class);
        return Objects.requireNonNull(response.getBody()).getBalance();
    }

    @Override
    public void fundTransfer(String beneficiaryAccountNumber, BigDecimal amount) {
        UserDetails userDetails = getSignedInUserDetails();
        FundTransferRequest request = new FundTransferRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setBeneficiaryAccountNumber(beneficiaryAccountNumber);
        request.setAmount(amount);

        try {
            HttpEntity<FundTransferRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<FundTransferResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + FUND_TRANSFER_ENDPOINT,
                    requestEntity,
                    FundTransferResponse.class);

            UserDetails sourceUserDetails = userDetailsRepository
                    .findByAccountNumber(getAccountNumber(
                            Objects.requireNonNull(response.getBody()).getBeneficiaryTransaction().getTransactionDescription()))
                    .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

            UserDetails beneficiaryUserDetails = userDetailsRepository
                    .findByAccountNumber(getAccountNumber(
                            Objects.requireNonNull(response.getBody().getSourceTransaction().getTransactionDescription())))
                    .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

            Transaction sourceTransaction = modelMapperUtility.initialize()
                    .map(response.getBody().getSourceTransaction(), Transaction.class);
            sourceTransaction.setTransactionDate(LocalDate.parse(response.getBody().getSourceTransaction().getTransactionDate()));
            sourceTransaction.setCorporateNumber(getCorporateNumber(response.getBody().getSourceTransaction().getTransactionDescription()));
            sourceTransaction.setAccountNumber(getAccountNumber(response.getBody().getSourceTransaction().getTransactionDescription()));
            sourceTransaction.setTransactionMessage(getTransactionMessage(response.getBody().getSourceTransaction().getTransactionDescription()));
            sourceTransaction.setUser(sourceUserDetails.getUser());

            Transaction beneficiaryTransaction = modelMapperUtility.initialize()
                    .map(response.getBody().getBeneficiaryTransaction(), Transaction.class);
            beneficiaryTransaction.setTransactionDate(LocalDate.parse(response.getBody().getBeneficiaryTransaction().getTransactionDate()));
            beneficiaryTransaction.setCorporateNumber(getCorporateNumber(response.getBody().getBeneficiaryTransaction().getTransactionDescription()));
            beneficiaryTransaction.setAccountNumber(getAccountNumber(response.getBody().getBeneficiaryTransaction().getTransactionDescription()));
            beneficiaryTransaction.setTransactionMessage(getTransactionMessage(response.getBody().getBeneficiaryTransaction().getTransactionDescription()));
            beneficiaryTransaction.setUser(beneficiaryUserDetails.getUser());

            transactionRepository.save(sourceTransaction);
            transactionRepository.save(beneficiaryTransaction);
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new TransferFailedException(error);
        }
    }

    private UserDetails getSignedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userDetailsRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));
    }

    private String getCorporateNumber(String transactionDescription) {
        String[] numbers = transactionDescription.split("/");
        return numbers[0];
    }

    private String getAccountNumber(String transactionDescription) {
        String[] numbers = transactionDescription.split("/");
        return numbers[1];
    }

    private String getTransactionMessage(String transactionDescription) {
        String[] numbers = transactionDescription.split("/");
        return numbers[2];
    }
}