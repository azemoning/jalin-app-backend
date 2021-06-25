package com.jalin.jalinappbackend.module.banking.model;

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
public class TransactionDto {
    private String transactionName;
    private String corporateId;
    private String corporateName;
    private String accountNumber;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private String transactionType;
    private String currency;
    private BigDecimal amount;
    private String transactionMessage;
    private String transactionNote;
}
