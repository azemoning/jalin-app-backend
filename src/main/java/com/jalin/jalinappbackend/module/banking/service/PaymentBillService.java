package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;

import java.util.UUID;

public interface PaymentBillService {
    PrepaidElectricityDto getElectricityPrepaidOptions(String customerId);
    ConfirmPaymentDetailsDto confirmPaymentElectricityPrepaid(String customerId, UUID prepaidId);
}
