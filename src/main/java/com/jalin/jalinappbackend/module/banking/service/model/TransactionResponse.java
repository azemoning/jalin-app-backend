package com.jalin.jalinappbackend.module.banking.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionResponse {
    private String transactionId;
    private String transactionDate;
    private String transactionType;
    private String currency;
    private BigDecimal amount;
    private String transactionName;
    private String transactionDescription;

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.UNNECESSARY);
    }
}
