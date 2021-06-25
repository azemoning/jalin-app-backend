package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.banking.repository.model.TransactionAggregation;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CorporateService corporateService;

    @Override
    public List<TransactionDto> getAllTransactions() {
        User user = getSignedInUser();
        List<Transaction> transactionList = transactionRepository.findByUserOrderByCreatedDateAsc(user);
        List<TransactionDto> transactionDtoSet = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            TransactionDto transactionDto = modelMapperUtility.initialize().map(transaction, TransactionDto.class);
            transactionDto.setCorporateName(corporateService.getCorporateByCorporateId(transaction.getCorporateId()).getCorporateName());
            transactionDto.setTransactionTime(LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.of("Asia/Ho_Chi_Minh")));
            transactionDtoSet.add(transactionDto);
        }
        return transactionDtoSet;
    }

    @Override
    public List<TransactionAggregation> getMostFrequentTransactions() {
        User user = getSignedInUser();
        return transactionRepository.findMostFrequentTransactions(user);
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
