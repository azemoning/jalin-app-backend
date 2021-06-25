package com.jalin.jalinappbackend.module.banking.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionAggregation {
    private String transactionName;
    private String corporateId;
    private String accountNumber;
    private Long count;
}
