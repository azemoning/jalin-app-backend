package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.BalanceDto;
import com.jalin.jalinappbackend.module.banking.model.ConfirmTransferDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;

import java.math.BigDecimal;

public interface BankingService {
    BalanceDto getAccountBalance();
    ConfirmTransferDto confirmTransfer(String corporateId, String beneficiaryAccountNumber, BigDecimal amount);
    TransactionDto fundTransfer(String beneficiaryAccountNumber, BigDecimal amount, String transactionNote);
    TransactionDto fundTransferDomestic(String corporateId, String beneficiaryAccountNumber, BigDecimal amount, String transactionNote);
}
