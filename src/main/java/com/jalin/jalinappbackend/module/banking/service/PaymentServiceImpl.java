package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.service.model.PaymentQrRequest;
import com.jalin.jalinappbackend.module.banking.service.model.PaymentQrResponse;
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
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${resource.server.url}")
    private String BASE_URL;
    private static final String PAYMENT_QR_ENDPOINT = "/api/v1/payment/qr";
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

    @Override
    public ConfirmPaymentDto confirmPayment(String corporateId, BigDecimal amount) {
        return initializeConfirmTransferDto(
                corporateId,
                corporateService.getCorporateByCorporateId(corporateId).getCorporateName(),
                amount,
                IDR_NO_PAYMENT_FEE,
                IDR_NO_PAYMENT_DISCOUNT);
    }

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
            return transactionDto;
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new ResourceNotFoundException(error);
        }
    }

    private ConfirmPaymentDto initializeConfirmTransferDto(
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
