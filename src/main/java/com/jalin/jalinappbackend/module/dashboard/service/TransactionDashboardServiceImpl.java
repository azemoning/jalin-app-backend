package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.service.CorporateService;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionCountDto;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionDetailsDto;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionDto;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionMostFrequentDto;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionDashboardServiceImpl implements TransactionDashboardService {
    private static final String TRANSFER_TRANSACTION_NAME = "TRANSFER";
    private static final String TRANSFER_DOMESTIC_TRANSACTION_NAME = "TRANSFER_DOMESTIC";
    private static final String TOP_UP_TRANSACTION_NAME = "TOP_UP";
    private static final String PAYMENT_QR_TRANSACTION_NAME = "PAYMENT_QR";

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CorporateService corporateService;

    @Override
    public List<TransactionDto> getAllTransactions(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        List<Transaction> transactionList = transactionPage.getContent();

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(transaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService
                    .getCorporateByCorporateId(transaction.getCorporateId())
                    .getCorporateName());
            transactionDto.setTransactionTime(
                    LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.of("Asia/Ho_Chi_Minh")));
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    @Override
    public TransactionDetailsDto getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Transaction with ID %s not found", transactionId)));

        TransactionDetailsDto transactionDetailsDto = modelMapperUtility.initialize()
                .map(transaction, TransactionDetailsDto.class);
        transactionDetailsDto.setCorporateName(corporateService
                .getCorporateByCorporateId(transaction.getCorporateId())
                .getCorporateName());
        transactionDetailsDto.setBeneficiaryAccountNumber(transaction.getAccountNumber());
        transactionDetailsDto.setTransactionTime(
                LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.of("Asia/Ho_Chi_Minh")));

        UserDetails userDetails = userDetailsRepository.findByUser(transaction.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        transactionDetailsDto.setSourceAccountNumber(userDetails.getAccountNumber());
        return transactionDetailsDto;
    }

    @Override
    public TransactionMostFrequentDto getMostFrequentTransactions() {
        Integer totalTransactions = transactionRepository.findAll().size();

        List<TransactionCountDto> transactionCountDtoList = new ArrayList<>();

        TransactionCountDto transactionCountDtoTransfer = initiateTransactionCountDto(
                TRANSFER_TRANSACTION_NAME, totalTransactions);
        transactionCountDtoList.add(transactionCountDtoTransfer);

        TransactionCountDto transactionCountDtoTransferDomestic = initiateTransactionCountDto(
                TRANSFER_DOMESTIC_TRANSACTION_NAME, totalTransactions);
        transactionCountDtoList.add(transactionCountDtoTransferDomestic);

        TransactionCountDto transactionCountDtoTopUp = initiateTransactionCountDto(
                TOP_UP_TRANSACTION_NAME, totalTransactions);
        transactionCountDtoList.add(transactionCountDtoTopUp);

        TransactionCountDto transactionCountDtoPaymentQr = initiateTransactionCountDto(
                PAYMENT_QR_TRANSACTION_NAME, totalTransactions);
        transactionCountDtoList.add(transactionCountDtoPaymentQr);

        return new TransactionMostFrequentDto(totalTransactions, transactionCountDtoList);
    }

    private TransactionCountDto initiateTransactionCountDto(String transactionName, Integer totalTransactions) {
        Long transactionCount = transactionRepository.countByTransactionName(transactionName);
        return new TransactionCountDto(transactionName, transactionCount, totalTransactions);
    }
}
