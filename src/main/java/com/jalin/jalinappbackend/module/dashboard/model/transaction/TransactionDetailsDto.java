package com.jalin.jalinappbackend.module.dashboard.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionDetailsDto {
    private String transactionId;
    private String transactionName;
    private String sourceAccountNumber;
    private String sourceAccountCustomerName;
    private String corporateName;
    private String beneficiaryAccountNumber;
    private String beneficiaryAccountCustomerName;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private String transactionType;
    private String currency;
    private BigDecimal amount;
    private String transactionMessage;
    private String transactionNote;
}
