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
public class PaymentMobilePhoneCreditRequest {
    private String sourceAccountNumber;
    private String corporateId;
    private String mobilePhoneNumber;
    private BigDecimal amount;
}
