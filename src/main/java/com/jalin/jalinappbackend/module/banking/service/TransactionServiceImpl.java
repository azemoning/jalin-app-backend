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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public Set<TransactionDto> getAllTransactions() {
        User user = getSignedInUser();
        List<Transaction> transactionList = transactionRepository.findByUser(user);
        Set<TransactionDto> transactionDtoSet = new HashSet<>();
        for (Transaction transaction : transactionList) {
            TransactionDto transactionDto = modelMapperUtility.initialize().map(transaction, TransactionDto.class);
            transactionDto.setTransactionTime(LocalTime.ofInstant(transaction.getCreatedDate(), ZoneId.systemDefault()));
            transactionDtoSet.add(transactionDto);
        }
        return transactionDtoSet;
    }

    @Override
    public Set<TransactionAggregation> getMostFrequentTransactions() {
        User user = getSignedInUser();
        List<TransactionAggregation> transactionAggregationList = transactionRepository.findMostFrequentTransactions(user);
        return new HashSet<>(transactionAggregationList);
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
