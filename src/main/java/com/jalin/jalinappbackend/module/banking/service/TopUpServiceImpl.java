package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.exception.TransferFailedException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.entity.WalletList;
import com.jalin.jalinappbackend.module.banking.model.ConfirmTransferDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.repository.WalletListRepository;
import com.jalin.jalinappbackend.module.banking.service.model.FundTransferVirtualRequest;
import com.jalin.jalinappbackend.module.banking.service.model.FundTransferVirtualResponse;
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
public class TopUpServiceImpl implements TopUpService {
    @Value("${resource.server.url}")
    private String BASE_URL;

    private static final String FUND_TRANSFER_VIRTUAL_ENDPOINT = "/v1/transfers/virtual";
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
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletListRepository walletListRepository;
    @Autowired
    private CorporateService corporateService;

    @Autowired
    private UserMissionService userMissionService;

    @Override
    public ConfirmTransferDto confirmTransfer(String corporateId, String beneficiaryAccountNumber, BigDecimal amount) {
        WalletList walletList = walletListRepository
                .findByUserAndCorporateIdAndAccountNumber(
                        userUtility.getSignedInUser(), corporateId, beneficiaryAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Digital wallet account not found in wallet list"));
        return initializeConfirmTransferDto(
                corporateId,
                corporateService.getCorporateByCorporateId(corporateId).getCorporateName(),
                beneficiaryAccountNumber,
                walletList.getFullName(),
                amount,
                IDR_NO_TRANSFER_FEE);
    }

    @Override
    public TransactionDto fundTransferVirtual(String corporateId, String beneficiaryAccountNumber, BigDecimal amount, String transactionNote) {
        UserDetails userDetails = userDetailsRepository.findByUser(userUtility.getSignedInUser())
                .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

        FundTransferVirtualRequest request = new FundTransferVirtualRequest();
        request.setSourceAccountNumber(userDetails.getAccountNumber());
        request.setCorporateId(corporateId);
        request.setBeneficiaryAccountNumber(beneficiaryAccountNumber);
        request.setAmount(amount);

        try {
            HttpEntity<FundTransferVirtualRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<FundTransferVirtualResponse> response = restTemplateUtility.initialize().postForEntity(
                    BASE_URL + FUND_TRANSFER_VIRTUAL_ENDPOINT,
                    requestEntity,
                    FundTransferVirtualResponse.class);

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
