package com.jalin.jalinappbackend.module.banking.service;

import java.math.BigDecimal;

public interface BankingService {
    BigDecimal getAccountBalance();
    void fundTransfer(String beneficiaryAccountNumber, BigDecimal amount);
}
