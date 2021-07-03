package com.jalin.jalinappbackend.module.banking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfirmPaymentDto {
    private String corporateId;
    private String corporateName;
    private BigDecimal paymentAmount;
    private BigDecimal paymentFee;
    private BigDecimal paymentDiscount;
    private BigDecimal totalPaymentAmount;
}
