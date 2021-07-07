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
public class PaymentQrRequest {
    private String sourceAccountNumber;
    private String corporateId;
    private BigDecimal amount;
}
