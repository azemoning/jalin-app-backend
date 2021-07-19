package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidMobilePhoneDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    TransactionDto payWithQr(String corporateId, BigDecimal amount, String transactionNote);
    ConfirmPaymentDto confirmPaymentQr(String corporateId, BigDecimal amount);

    PrepaidMobilePhoneDto getMobilePhoneCreditOptions(String mobilePhoneNumber);
    PrepaidMobilePhoneDto getMobilePhonePrepaidOptions(String mobilePhoneNumber);
    TransactionDto payMobilePhoneCredit(String providerId, String mobilePhoneNumber, BigDecimal amount);
    TransactionDto payMobilePhoneData(String providerId, String mobilePhoneNumber, BigDecimal amount);
    ConfirmPaymentDetailsDto confirmPaymentMobilePhoneCredit(String providerId, UUID prepaidId, String mobilePhoneNumber);
    ConfirmPaymentDetailsDto confirmPaymentMobilePhoneData(String providerId, UUID prepaidId, String mobilePhoneNumber);
}
