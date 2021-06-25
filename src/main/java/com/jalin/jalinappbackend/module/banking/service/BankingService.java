package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.ConfirmTransferDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;

import java.math.BigDecimal;

public interface BankingService {
    BigDecimal getAccountBalance();
    ConfirmTransferDto confirmTransfer(String corporateId, String beneficiaryAccountNumber, BigDecimal amount);
    TransactionDto fundTransfer(String beneficiaryAccountNumber, BigDecimal amount, String transactionNote);
    TransactionDto fundTransferDomestic(String corporateId, String beneficiaryAccountNumber, BigDecimal amount, String transactionNote);
}
