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
public class ConfirmTransferDto {
    private String corporateId;
    private String corporateName;
    private String accountNumber;
    private String fullName;
    private BigDecimal transferAmount;
    private BigDecimal transferFee;
    private BigDecimal totalTransferAmount;
}
