package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.exception.TransferFailedException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.entity.TransferList;
import com.jalin.jalinappbackend.module.banking.model.BalanceDto;
import com.jalin.jalinappbackend.module.banking.model.ConfirmTransferDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.repository.TransferListRepository;
import com.jalin.jalinappbackend.module.banking.service.model.*;
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
import java.util.Objects;

@Service
public class BankingServiceImpl implements BankingService {
    @Value("${resource.server.url}")
    private String BASE_URL;

    private static final String GET_BANK_ACCOUNT_ENDPOINT = "/v1/accounts/";
    private static final String FUND_TRANSFER_ENDPOINT = "/v1/transfers";
    private static final String FUND_TRANSFER_DOMESTIC_ENDPOINT = "/v1/transfers/domestic";
    private static final BigDecimal IDR_TRANSFER_FEE = new BigDecimal("6500").setScale(2, RoundingMode.UNNECESSARY);
    private static final BigDecimal IDR_NO_TRANSFER_FEE = new BigDecimal("0").setScale(2, RoundingMode.UNNECESSARY);
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserUtility userUtility;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TransferListRepository transferListRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CorporateService corporateService;

    @Autowired
    private UserMissionService userMissionService;

    @Override
    public BalanceDto getAccountBalance() {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        ResponseEntity<GetBankAccountResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_BANK_ACCOUNT_ENDPOINT + userDetails.getAccountNumber(),
                GetBankAccountResponse.class);
        return new BalanceDto(Objects.requireNonNull(response.getBody()).getBalance());
    }

    @Override
    public ConfirmTransferDto confirmTransfer(String corporateId, String beneficiaryAccountNumber, BigDecimal amount) {
        TransferList transferList = transferListRepository
                .findByUserAndCorporateIdAndAccountNumber(
                        userUtility.getSignedInUser(), corporateId, beneficiaryAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found in transfer list"));

        if (!corporateId.equals("212")) {
            return initializeConfirmTransferDto(
                    corporateId,
                    corporateService.getCorporateByCorporateId(corporateId).getCorporateName(),
                    beneficiaryAccountNumber,
                    transferList.getFullName(),
                    amount,
                    IDR_TRANSFER_FEE);
        } else {
            return initializeConfirmTransferDto(
                    corporateId,
                    corporateService.getCorporateByCorporateId(corporateId).getCorporateName(),
                    beneficiaryAccountNumber,
                    transferList.getFullName(),
                    amount,
                    IDR_NO_TRANSFER_FEE);
        }
    }

    @Override
    public TransactionDto fundTransfer(String beneficiaryAccountNumber, BigDecimal amount, String transactionNote) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

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
            sourceTransaction.setCorporateId(getCorporateId(response.getBody().getSourceTransaction().getTransactionDescription()));
            sourceTransaction.setAccountNumber(getAccountNumber(response.getBody().getSourceTransaction().getTransactionDescription()));
            sourceTransaction.setTransactionMessage(getTransactionMessage(response.getBody().getSourceTransaction().getTransactionDescription()));
            sourceTransaction.setTransactionNote(transactionNote);
            sourceTransaction.setUser(sourceUserDetails.getUser());

            Transaction beneficiaryTransaction = modelMapperUtility.initialize()
                    .map(response.getBody().getBeneficiaryTransaction(), Transaction.class);
            beneficiaryTransaction.setTransactionDate(LocalDate.parse(response.getBody().getBeneficiaryTransaction().getTransactionDate()));
            beneficiaryTransaction.setCorporateId(getCorporateId(response.getBody().getBeneficiaryTransaction().getTransactionDescription()));
            beneficiaryTransaction.setAccountNumber(getAccountNumber(response.getBody().getBeneficiaryTransaction().getTransactionDescription()));
            beneficiaryTransaction.setTransactionMessage(getTransactionMessage(response.getBody().getBeneficiaryTransaction().getTransactionDescription()));
            beneficiaryTransaction.setTransactionNote(transactionNote);
            beneficiaryTransaction.setUser(beneficiaryUserDetails.getUser());

            Transaction savedSourceTransaction = transactionRepository.save(sourceTransaction);
            transactionRepository.save(beneficiaryTransaction);

            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(savedSourceTransaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService.getCorporateByCorporateId(savedSourceTransaction.getCorporateId()).getCorporateName());
            transactionDto.setTransactionTime(LocalTime.ofInstant(savedSourceTransaction.getCreatedDate(), ZoneId.of("Asia/Ho_Chi_Minh")));
            userMissionService.checkUserMissionProgress();
            return transactionDto;
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new TransferFailedException(error);
        }
    }

    @Override
    public TransactionDto fundTransferDomestic(String corporateId, String beneficiaryAccountNumber, BigDecimal amount, String transactionNote) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        FundTransferDomesticRequest request = new FundTransferDomesticRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setCorporateId(corporateId);
        request.setBeneficiaryAccountNumber(beneficiaryAccountNumber);
        request.setAmount(amount);

        try {
            HttpEntity<FundTransferDomesticRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<FundTransferDomesticResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + FUND_TRANSFER_DOMESTIC_ENDPOINT,
                    requestEntity,
                    FundTransferDomesticResponse.class);

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
            throw new TransferFailedException(error);
        }
    }

    private ConfirmTransferDto initializeConfirmTransferDto(
            String corporateId,
            String corporateName,
            String accountNumber,
            String fullName,
            BigDecimal transferAmount,
            BigDecimal transferFee) {
        return new ConfirmTransferDto(
                corporateId,
                corporateName,
                accountNumber,
                fullName,
                transferAmount,
                transferFee,
                transferAmount.add(transferFee));
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
