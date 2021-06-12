package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.model.TransactionAggregation;

import java.util.Set;

public interface TransactionService {
    Set<TransactionDto> getAllTransactions();
    Set<TransactionAggregation> getMostFrequentTransactions();
}
