package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.model.TransactionAggregation;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAllTransactions();
    List<TransactionAggregation> getMostFrequentTransactions();
}
