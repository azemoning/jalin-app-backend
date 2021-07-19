package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentBillService {
    PrepaidElectricityDto getElectricityPrepaidOptions(String customerId);
    ConfirmPaymentDetailsDto confirmPaymentElectricityPrepaid(String customerId, UUID prepaidId);
    ConfirmPaymentDetailsDto confirmPaymentElectricityPostpaid(String customerId);
    TransactionDto payElectricityPrepaid(String customerId, BigDecimal amount);
    TransactionDto payElectricityPostpaid(String customerId, BigDecimal amount);
}
