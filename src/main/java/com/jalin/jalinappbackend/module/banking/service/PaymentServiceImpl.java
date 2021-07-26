package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.MobilePhoneNumberNotValidException;
import com.jalin.jalinappbackend.exception.PaymentFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.model.*;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.service.model.payment.*;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${resource.server.url}")
    private String BASE_URL;

    private static final String PAYMENT_QR_ENDPOINT = "/v1/payment/qr";
    private static final String PAYMENT_MOBILE_PHONE_CREDIT = "/v1/payment/mobile/prepaid/credit";
    private static final String PAYMENT_MOBILE_PHONE_DATA = "/v1/payment/mobile/prepaid/data";

    private static final String GET_PROVIDER_BY_ID = "/v1/providers/";
    private static final String GET_PROVIDER_BY_PREFIX_ENDPOINT = "/v1/providers/find?prefixNumber=";
    private static final String GET_MOBILE_PHONE_CREDIT_OPTIONS = "/v1/prepaid/mobile/credit";
    private static final String GET_MOBILE_PHONE_DATA_OPTIONS = "/v1/prepaid/mobile/data";
    private static final String GET_MOBILE_PHONE_CREDIT_OPTION_BY_ID = "/v1/prepaid/mobile/credit/";
    private static final String GET_MOBILE_PHONE_DATA_OPTION_BY_ID = "/v1/prepaid/mobile/data/";

    private static final BigDecimal IDR_NO_PAYMENT_FEE = new BigDecimal("0").setScale(2, RoundingMode.UNNECESSARY);
    private static final BigDecimal IDR_NO_PAYMENT_DISCOUNT = new BigDecimal("0").setScale(2, RoundingMode.UNNECESSARY);

    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserUtility userUtility;

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CorporateService corporateService;

    @Autowired
    private UserMissionService userMissionService;

    @Override
    public TransactionDto payWithQr(String corporateId, BigDecimal amount, String transactionNote) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        PaymentQrRequest request = new PaymentQrRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setCorporateId(corporateId);
        request.setAmount(amount);

        try {
            HttpEntity<PaymentQrRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<PaymentQrResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + PAYMENT_QR_ENDPOINT,
                    requestEntity,
                    PaymentQrResponse.class);

            Transaction transaction = modelMapperUtility.initialize()
                    .map(Objects.requireNonNull(response.getBody()).getSourceTransaction(), Transaction.class);
            transaction.setTransactionDate(LocalDate.parse(Objects.requireNonNull(response.getBody()).getSourceTransaction().getTransactionDate()));
            transaction.setCorporateId(getCorporateId(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setAccountNumber(getAccountNumber(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setTransactionMessage(getTransactionMessage(response.getBody().getSourceTransaction().getTransactionDescription()));
            transaction.setTransactionNote(transactionNote);
            transaction.setUser(userDetails.getUser());

            Transaction savedTransaction = transactionRepository.save(transaction);
            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(savedTransaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService.getCorporateByCorporateId(savedTransaction.getCorporateId()).getCorporateName());
            transactionDto.setTransactionTime(LocalTime.ofInstant(savedTransaction.getCreatedDate(), ZoneId.of("Asia/Ho_Chi_Minh")));
            userMissionService.checkUserMissionProgress();
            return transactionDto;
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new PaymentFailedException(error);
        }
    }

    @Override
    public ConfirmPaymentDto confirmPaymentQr(String corporateId, BigDecimal amount) {
        return initializeConfirmPaymentDto(
                corporateId,
                corporateService.getCorporateByCorporateId(corporateId).getCorporateName(),
                amount,
                IDR_NO_PAYMENT_FEE,
                IDR_NO_PAYMENT_DISCOUNT);
    }

    @Override
    public PrepaidMobilePhoneDto getMobilePhoneCreditOptions(String mobilePhoneNumber) {
        ProviderDto providerDto = getMobilePhoneProvider(mobilePhoneNumber);

        ResponseEntity<GetPrepaidOptionResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_MOBILE_PHONE_CREDIT_OPTIONS,
                GetPrepaidOptionResponse.class);

        List<PrepaidDto> prepaidDtoList = new ArrayList<>();
        for (PrepaidOption prepaidOption : Objects.requireNonNull(response.getBody()).getPrepaidOptionList()) {
            PrepaidDto prepaidDto = modelMapperUtility.initialize()
                    .map(prepaidOption, PrepaidDto.class);
            prepaidDtoList.add(prepaidDto);
        }

        PrepaidMobilePhoneDto prepaidMobilePhoneDto = modelMapperUtility.initialize()
                .map(providerDto, PrepaidMobilePhoneDto.class);
        prepaidMobilePhoneDto.setCreditList(prepaidDtoList);
        return prepaidMobilePhoneDto;
    }

    @Override
    public PrepaidMobilePhoneDto getMobilePhonePrepaidOptions(String mobilePhoneNumber) {
        ProviderDto providerDto = getMobilePhoneProvider(mobilePhoneNumber);

        ResponseEntity<GetPrepaidOptionResponse> responseCreditOptions = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_MOBILE_PHONE_CREDIT_OPTIONS,
                GetPrepaidOptionResponse.class);

        ResponseEntity<GetPrepaidOptionDetailsResponse> responseDataOptions = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_MOBILE_PHONE_DATA_OPTIONS,
                GetPrepaidOptionDetailsResponse.class);

        List<PrepaidDto> creditList = new ArrayList<>();
        for (PrepaidOption prepaidOption : Objects.requireNonNull(responseCreditOptions.getBody()).getPrepaidOptionList()) {
            PrepaidDto prepaidDto = modelMapperUtility.initialize()
                    .map(prepaidOption, PrepaidDto.class);
            creditList.add(prepaidDto);
        }

        List<PrepaidDetailsDto> dataList = new ArrayList<>();
        for (PrepaidOptionDetails prepaidOptionDetails : Objects.requireNonNull(responseDataOptions.getBody()).getPrepaidOptionDetailsList()) {
            PrepaidDetailsDto prepaidDetailsDto = modelMapperUtility.initialize()
                    .map(prepaidOptionDetails, PrepaidDetailsDto.class);
            dataList.add(prepaidDetailsDto);
        }

        PrepaidMobilePhoneDto prepaidMobilePhoneDto = modelMapperUtility.initialize()
                .map(providerDto, PrepaidMobilePhoneDto.class);
        prepaidMobilePhoneDto.setCreditList(creditList);
        prepaidMobilePhoneDto.setDataList(dataList);
        return prepaidMobilePhoneDto;
    }

    @Override
    public TransactionDto payMobilePhoneCredit(String providerId, String mobilePhoneNumber, BigDecimal amount) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        PaymentMobilePhoneCreditRequest request = new PaymentMobilePhoneCreditRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setCorporateId(providerId);
        request.setMobilePhoneNumber(mobilePhoneNumber);
        request.setAmount(amount);

        try {
            HttpEntity<PaymentMobilePhoneCreditRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<PaymentMobilePhoneCreditResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + PAYMENT_MOBILE_PHONE_CREDIT,
                    requestEntity,
                    PaymentMobilePhoneCreditResponse.class);

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
    public TransactionDto payMobilePhoneData(String providerId, String mobilePhoneNumber, BigDecimal amount) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        PaymentMobilePhoneDataRequest request = new PaymentMobilePhoneDataRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setCorporateId(providerId);
        request.setMobilePhoneNumber(mobilePhoneNumber);
        request.setAmount(amount);

        try {
            HttpEntity<PaymentMobilePhoneDataRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<PaymentMobilePhoneDataResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + PAYMENT_MOBILE_PHONE_DATA,
                    requestEntity,
                    PaymentMobilePhoneDataResponse.class);

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
    public ConfirmPaymentDetailsDto confirmPaymentMobilePhoneCredit(String providerId, UUID prepaidId, String mobilePhoneNumber) {
        try {
            ResponseEntity<GetProviderResponse> responseProvider = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_PROVIDER_BY_ID + providerId,
                    GetProviderResponse.class);

            ResponseEntity<PrepaidOption> responsePrepaid = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_MOBILE_PHONE_CREDIT_OPTION_BY_ID + prepaidId,
                    PrepaidOption.class);

            return initializeConfirmPaymentDetailsDto(
                    Objects.requireNonNull(responseProvider.getBody()).getProviderId(),
                    corporateService.getCorporateByCorporateId(providerId).getCorporateName(),
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
    public ConfirmPaymentDetailsDto confirmPaymentMobilePhoneData(String providerId, UUID prepaidId, String mobilePhoneNumber) {
        try {
            ResponseEntity<GetProviderResponse> responseProvider = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_PROVIDER_BY_ID + providerId,
                    GetProviderResponse.class);

            ResponseEntity<PrepaidOptionDetails> responsePrepaid = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_MOBILE_PHONE_DATA_OPTION_BY_ID + prepaidId,
                    PrepaidOptionDetails.class);

            return initializeConfirmPaymentDetailsDto(
                    Objects.requireNonNull(responseProvider.getBody()).getProviderId(),
                    corporateService.getCorporateByCorporateId(providerId).getCorporateName(),
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

    private ConfirmPaymentDto initializeConfirmPaymentDto(
            String corporateId,
            String corporateName,
            BigDecimal transferAmount,
            BigDecimal transferFee,
            BigDecimal transferDiscount) {
        return new ConfirmPaymentDto(
                corporateId,
                corporateName,
                transferAmount,
                transferFee,
                transferDiscount,
                transferAmount.add(transferFee).subtract(transferDiscount));
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

    private ProviderDto getMobilePhoneProvider(String mobilePhoneNumber) {
        if (mobilePhoneNumber.length() > 12 | mobilePhoneNumber.length() < 11) {
            throw new MobilePhoneNumberNotValidException("Mobile number not valid");
        }

        try {
            ResponseEntity<GetProviderResponse> response = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_PROVIDER_BY_PREFIX_ENDPOINT + mobilePhoneNumber.substring(0, 4),
                    GetProviderResponse.class);

            ProviderDto providerDto = modelMapperUtility.initialize().map(response.getBody(), ProviderDto.class);
            providerDto.setMobilePhoneNumber(mobilePhoneNumber);
            return providerDto;
        } catch (HttpClientErrorException exception) {
            throw new MobilePhoneNumberNotValidException("Mobile number not valid");
        }
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
