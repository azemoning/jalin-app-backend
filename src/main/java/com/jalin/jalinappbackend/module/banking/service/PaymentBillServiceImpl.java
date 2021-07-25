package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.CustomerIdNotValidException;
import com.jalin.jalinappbackend.exception.PaymentFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.service.model.payment.GetPrepaidOptionResponse;
import com.jalin.jalinappbackend.module.banking.service.model.payment.PaymentElectricityRequest;
import com.jalin.jalinappbackend.module.banking.service.model.payment.PaymentElectricityResponse;
import com.jalin.jalinappbackend.module.banking.service.model.payment.PrepaidOption;
import com.jalin.jalinappbackend.utility.FakerUtility;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import com.jalin.jalinappbackend.utility.UserUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PaymentBillServiceImpl implements PaymentBillService {
    @Value("${resource.server.url}")
    private String BASE_URL;

    private static final String PAYMENT_ELECTRICITY_PREPAID = "/v1/payment/electricity/prepaid";
    private static final String PAYMENT_ELECTRICITY_POSTPAID = "/v1/payment/electricity/postpaid";
    private static final String GET_ELECTRICITY_PREPAID_OPTIONS = "/v1/prepaid/electricity";
    private static final String GET_ELECTRICITY_PREPAID_OPTION_BY_ID = "/v1/prepaid/electricity/";

    private static final String PLN_CORPORATE_ID = "10123";
    private static final BigDecimal IDR_NO_PAYMENT_FEE = new BigDecimal("0").setScale(2, RoundingMode.UNNECESSARY);
    private static final BigDecimal IDR_NO_PAYMENT_DISCOUNT = new BigDecimal("0").setScale(2, RoundingMode.UNNECESSARY);

    private static final Integer MIN_POSTPAID_BILL = 200000;
    private static final Integer MAX_POSTPAID_BILL = 2500000;
    private static final String POSTPAID_PAYMENT_DETAILS = "Electricity postpaid bill";

    @Autowired
    private FakerUtility fakerUtility;
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserUtility userUtility;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private CorporateService corporateService;

    @Override
    public PrepaidElectricityDto getElectricityPrepaidOptions(String customerId) {
        validateCustomerId(customerId);

        ResponseEntity<GetPrepaidOptionResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_ELECTRICITY_PREPAID_OPTIONS,
                GetPrepaidOptionResponse.class);

        List<PrepaidDto> prepaidDtoList = new ArrayList<>();
        for (PrepaidOption prepaidOption : Objects.requireNonNull(response.getBody()).getPrepaidOptionList()) {
            PrepaidDto prepaidDto = modelMapperUtility.initialize()
                    .map(prepaidOption, PrepaidDto.class);
            prepaidDtoList.add(prepaidDto);
        }

        PrepaidElectricityDto prepaidElectricityDto = new PrepaidElectricityDto();
        prepaidElectricityDto.setCustomerId(customerId);
        prepaidElectricityDto.setCustomerName(fakerUtility.initialize().name().fullName());
        prepaidElectricityDto.setTokenList(prepaidDtoList);
        return prepaidElectricityDto;
    }

    @Override
    public ConfirmPaymentDetailsDto confirmPaymentElectricityPrepaid(String customerId, UUID prepaidId) {
        validateCustomerId(customerId);

        try {
            ResponseEntity<PrepaidOption> responsePrepaid = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_ELECTRICITY_PREPAID_OPTION_BY_ID + prepaidId,
                    PrepaidOption.class);

            return initializeConfirmPaymentDetailsDto(
                    PLN_CORPORATE_ID,
                    corporateService.getCorporateByCorporateId(PLN_CORPORATE_ID).getCorporateName(),
                    Objects.requireNonNull(responsePrepaid.getBody()).getPrice(),
                    IDR_NO_PAYMENT_FEE,
                    IDR_NO_PAYMENT_DISCOUNT,
                    responsePrepaid.getBody().getPrepaidName());
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new ResourceNotFoundException(error);
        }
    }

    @Override
    public ConfirmPaymentDetailsDto confirmPaymentElectricityPostpaid(String customerId) {
        validateCustomerId(customerId);
        return initializeConfirmPaymentDetailsDto(
                PLN_CORPORATE_ID,
                corporateService.getCorporateByCorporateId(PLN_CORPORATE_ID).getCorporateName(),
                generatePostpaidBill(),
                IDR_NO_PAYMENT_FEE,
                IDR_NO_PAYMENT_DISCOUNT,
                POSTPAID_PAYMENT_DETAILS);
    }

    @Override
    public TransactionDto payElectricityPrepaid(String customerId, BigDecimal amount) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        PaymentElectricityRequest request = new PaymentElectricityRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setCorporateId(PLN_CORPORATE_ID);
        request.setCustomerId(customerId);
        request.setAmount(amount);

        try {
            HttpEntity<PaymentElectricityRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<PaymentElectricityResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + PAYMENT_ELECTRICITY_PREPAID,
                    requestEntity,
                    PaymentElectricityResponse.class);

            Transaction transaction = modelMapperUtility.initialize()
                    .map(Objects.requireNonNull(response.getBody()).getSourceTransaction(), Transaction.class);
            transaction.setTransactionDate(LocalDate.parse(Objects.requireNonNull(response.getBody()).getSourceTransaction().getTransactionDate()));
            transaction.setCorporateId(getCorporateId(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setAccountNumber(getAccountNumber(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setTransactionMessage(getTransactionMessage(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setUser(userDetails.getUser());

            Transaction savedTransaction = transactionRepository.save(transaction);
            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(savedTransaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService.getCorporateByCorporateId(savedTransaction.getCorporateId()).getCorporateName());
            transactionDto.setTransactionTime(LocalTime.ofInstant(savedTransaction.getCreatedDate(), ZoneId.of("Asia/Ho_Chi_Minh")));
            return transactionDto;
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new PaymentFailedException(error);
        }
    }

    @Override
    public TransactionDto payElectricityPostpaid(String customerId, BigDecimal amount) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        PaymentElectricityRequest request = new PaymentElectricityRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setCorporateId(PLN_CORPORATE_ID);
        request.setCustomerId(customerId);
        request.setAmount(amount);

        try {
            HttpEntity<PaymentElectricityRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<PaymentElectricityResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + PAYMENT_ELECTRICITY_POSTPAID,
                    requestEntity,
                    PaymentElectricityResponse.class);

            Transaction transaction = modelMapperUtility.initialize()
                    .map(Objects.requireNonNull(response.getBody()).getSourceTransaction(), Transaction.class);
            transaction.setTransactionDate(LocalDate.parse(Objects.requireNonNull(response.getBody()).getSourceTransaction().getTransactionDate()));
            transaction.setCorporateId(getCorporateId(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setAccountNumber(getAccountNumber(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setTransactionMessage(getTransactionMessage(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setUser(userDetails.getUser());

            Transaction savedTransaction = transactionRepository.save(transaction);
            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(savedTransaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService.getCorporateByCorporateId(savedTransaction.getCorporateId()).getCorporateName());
            transactionDto.setTransactionTime(LocalTime.ofInstant(savedTransaction.getCreatedDate(), ZoneId.of("Asia/Ho_Chi_Minh")));
            return transactionDto;
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new PaymentFailedException(error);
        }
    }

    private void validateCustomerId(String customerId) {
        if (!customerId.matches("^[0-9]*$") | (customerId.length() != 12)) {
            throw new CustomerIdNotValidException("Customer ID not valid");
        }
    }

    private BigDecimal generatePostpaidBill() {
        Random random = new Random();
        int randomInteger = random.nextInt(MAX_POSTPAID_BILL - MIN_POSTPAID_BILL) + MIN_POSTPAID_BILL;
        return new BigDecimal(randomInteger);
    }

    private ConfirmPaymentDetailsDto initializeConfirmPaymentDetailsDto(
            String corporateId,
            String corporateName,
            BigDecimal transferAmount,
            BigDecimal transferFee,
            BigDecimal transferDiscount,
            Object transferDetails) {
        return new ConfirmPaymentDetailsDto(
                corporateId,
                corporateName,
                transferAmount,
                transferFee,
                transferDiscount,
                transferAmount.add(transferFee).subtract(transferDiscount),
                transferDetails);
    }

    private String getCorporateId(String transactionDescription) {
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
