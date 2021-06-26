package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.ConfirmTransferDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;

import java.math.BigDecimal;

public interface TopUpService {
    ConfirmTransferDto confirmTransfer(String corporateId, String beneficiaryAccountNumber, BigDecimal amount);
    TransactionDto fundTransferVirtual(String corporateId, String beneficiaryAccountNumber, BigDecimal amount, String transactionNote);
}
