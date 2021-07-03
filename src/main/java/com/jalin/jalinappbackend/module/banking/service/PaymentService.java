package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;

import java.math.BigDecimal;

public interface PaymentService {
    ConfirmPaymentDto confirmPayment(String corporateId, BigDecimal amount);
    TransactionDto payWithQr(String corporateId, BigDecimal amount, String transactionNote);
}
