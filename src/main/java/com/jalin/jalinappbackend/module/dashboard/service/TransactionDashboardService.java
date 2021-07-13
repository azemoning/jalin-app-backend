package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.TransactionAllDto;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionDetailsDto;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionMostFrequentDto;

import java.time.LocalDate;

public interface TransactionDashboardService {
    TransactionAllDto getAllTransactions(
            String[] transactionType,
            LocalDate startDate,
            LocalDate endDate,
            Integer page,
            Integer size,
            String[] sort);
    TransactionDetailsDto getTransactionById(String transactionId);
    TransactionMostFrequentDto getMostFrequentTransactions();
}
