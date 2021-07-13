package com.jalin.jalinappbackend.module.dashboard.model.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@Getter
@Setter
public class TransactionCountDto {
    private String transactionName;
    private Long transactionCount;
    private BigDecimal transactionCountPercentage;

    public TransactionCountDto(String transactionName, Long transactionCount, Integer totalTransaction) {
        this.transactionName = transactionName;
        this.transactionCount = transactionCount;
        this.transactionCountPercentage = BigDecimal.valueOf((float) transactionCount / (float) totalTransaction * 100)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
