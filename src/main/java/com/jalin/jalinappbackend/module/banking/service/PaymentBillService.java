package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;

public interface PaymentBillService {
    PrepaidElectricityDto getElectricityPrepaidOptions(String customerId);
}
