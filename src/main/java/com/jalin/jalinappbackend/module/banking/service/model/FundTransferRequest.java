package com.jalin.jalinappbackend.module.banking.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundTransferRequest {
    private String sourceAccountNumber;
    private String beneficiaryAccountNumber;
    private BigDecimal amount;
}
