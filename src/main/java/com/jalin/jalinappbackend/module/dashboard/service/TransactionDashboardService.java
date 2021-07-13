package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.transaction.TransactionAllDto;
import com.jalin.jalinappbackend.module.dashboard.model.transaction.TransactionDetailsDto;
import com.jalin.jalinappbackend.module.dashboard.model.transaction.TransactionMostFrequentDto;

import java.time.LocalDate;

public interface TransactionDashboardService {
    TransactionAllDto getAllTransactions(
            String[] transactionType,
            String[] transactionName,
            Integer page,
            Integer size,
            String[] sort,
            LocalDate startDate,
            LocalDate endDate,
            String keyword);
    TransactionDetailsDto getTransactionById(String transactionId);
    TransactionMostFrequentDto getMostFrequentTransactions();
}
