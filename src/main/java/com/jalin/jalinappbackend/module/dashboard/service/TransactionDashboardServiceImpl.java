package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.entity.TransferList;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.repository.TransferListRepository;
import com.jalin.jalinappbackend.module.banking.service.CorporateService;
import com.jalin.jalinappbackend.module.dashboard.model.transaction.*;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TransactionDashboardServiceImpl implements TransactionDashboardService {
    private static final String TRANSFER_TRANSACTION_NAME = "TRANSFER";
    private static final String TRANSFER_DOMESTIC_TRANSACTION_NAME = "TRANSFER_DOMESTIC";
    private static final String TOP_UP_TRANSACTION_NAME = "TOP_UP";
    private static final String PAYMENT_QR_TRANSACTION_NAME = "PAYMENT_QR";
    private static final String PAYMENT_MOBILE_PHONE_CREDIT_TRANSACTION_NAME = "PAYMENT_MOBILE_PHONE_CREDIT";

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransferListRepository transferListRepository;

    @Autowired
    private CorporateService corporateService;

    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(transaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService
                    .getCorporateByCorporateId(transaction.getCorporateId())
                    .getCorporateName());
            transactionDto.setTransactionTime(
                    LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.of("Asia/Jakarta")));
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    @Override
    public List<TransactionDto> getAllTransactions(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactionList = transactionRepository.findByTransactionDateBetween(startDate, endDate);

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(transaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService
                    .getCorporateByCorporateId(transaction.getCorporateId())
                    .getCorporateName());
            transactionDto.setTransactionTime(
                    LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.of("Asia/Jakarta")));
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    @Override
    public TransactionAllDto getAllTransactions(
            String[] transactionType,
            String[] transactionName,
            Integer page,
            Integer size,
            String[] sort,
            LocalDate startDate,
            LocalDate endDate,
            String keyword)  {

        List<String> names = new ArrayList<>();
        for (String name : transactionName) {
            names.add(name.toUpperCase(Locale.ROOT));
        }

        List<String> types = new ArrayList<>();
        for (String type : transactionType) {
            types.add(type.toUpperCase(Locale.ROOT));
        }

        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Transaction> transactionPage = transactionRepository
                .findByTransactionTypeInAndTransactionNameInAndTransactionDateBetweenAndTransactionMessageContainingIgnoringCase(
                        types, names, startDate, endDate, keyword, pageable);

        Long totalEntries = transactionPage.getTotalElements();
        Integer currentPage = transactionPage.getPageable().getPageNumber();
        Integer totalPages = transactionPage.getTotalPages();
        List<Transaction> transactionList = transactionPage.getContent();

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            TransactionDto transactionDto = modelMapperUtility.initialize()
                    .map(transaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService
                    .getCorporateByCorporateId(transaction.getCorporateId())
                    .getCorporateName());
            transactionDto.setTransactionTime(
                    LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.of("Asia/Jakarta")));
            transactionDtoList.add(transactionDto);
        }

        TransactionAllDto transactionAllDto = new TransactionAllDto();
        transactionAllDto.setTransactionType(types);
        transactionAllDto.setTransactionName(names);
        transactionAllDto.setTotalEntries(totalEntries);
        transactionAllDto.setCurrentPage(currentPage);
        transactionAllDto.setTotalPages(totalPages);
        transactionAllDto.setStartDate(startDate);
        transactionAllDto.setEndDate(endDate);
        transactionAllDto.setKeyword(keyword);
        transactionAllDto.setTransactionList(transactionDtoList);
        return transactionAllDto;
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
                LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.of("Asia/Jakarta")));

        UserDetails userDetails = userDetailsRepository.findByUser(transaction.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        transactionDetailsDto.setSourceAccountNumber(userDetails.getAccountNumber());
        transactionDetailsDto.setSourceAccountCustomerName(userDetails.getFullName());

        Optional<TransferList> transferList = transferListRepository.findByUserAndCorporateIdAndAccountNumber(
                transaction.getUser(),
                transaction.getCorporateId(),
                transaction.getAccountNumber());
        transactionDetailsDto.setBeneficiaryAccountCustomerName(transferList.map(TransferList::getFullName).orElse(null));

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

        TransactionCountDto transactionCountDtoPaymentMobilePhoneCredit = initiateTransactionCountDto(
                PAYMENT_MOBILE_PHONE_CREDIT_TRANSACTION_NAME, totalTransactions);
        transactionCountDtoList.add(transactionCountDtoPaymentMobilePhoneCredit);

        return new TransactionMostFrequentDto(totalTransactions, transactionCountDtoList);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    private TransactionCountDto initiateTransactionCountDto(String transactionName, Integer totalTransactions) {
        Long transactionCount = transactionRepository.countByTransactionName(transactionName);
        return new TransactionCountDto(transactionName, transactionCount, totalTransactions);
    }
}
