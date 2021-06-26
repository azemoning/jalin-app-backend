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
public class FundTransferVirtualRequest {
    private String sourceAccountNumber;
    private String corporateId;
    private String beneficiaryAccountNumber;
    private BigDecimal amount;
}
