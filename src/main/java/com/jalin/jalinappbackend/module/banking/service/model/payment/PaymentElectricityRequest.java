package com.jalin.jalinappbackend.module.banking.service.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentElectricityRequest {
    private String sourceAccountNumber;
    private String corporateId;
    private String customerId;
    private BigDecimal amount;
}
