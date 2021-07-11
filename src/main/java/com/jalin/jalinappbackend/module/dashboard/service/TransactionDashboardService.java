package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.TransactionDetailsDto;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionDto;

import java.util.List;

public interface TransactionDashboardService {
    List<TransactionDto> getAllTransactions(Integer page, Integer size);
    TransactionDetailsDto getTransactionById(String transactionId);
}
