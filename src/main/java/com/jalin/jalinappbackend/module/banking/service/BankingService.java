package com.jalin.jalinappbackend.module.banking.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;

public interface BankingService {
    BigDecimal getAccountBalance();
    void fundTransfer(String beneficiaryAccountNumber, BigDecimal amount);
}
