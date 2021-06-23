package com.jalin.jalinappbackend.module.banking.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundTransferDomesticResponse {
    private String transactionId;
    private String transactionDate;
    private String transactionType;
    private String currency;
    private BigDecimal amount;
    private String transactionName;
    private String transactionDescription;

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        this.transactionId = (String) data.get("transactionId");
        this.transactionDate = (String) data.get("transactionDate");
        this.transactionType = (String) data.get("transactionType");
        this.currency = (String) data.get("currency");
        this.amount = BigDecimal.valueOf((Integer) data.get("amount")).setScale(2, RoundingMode.UNNECESSARY);
        this.transactionName = (String) data.get("transactionName");
        this.transactionDescription = (String) data.get("transactionDescription");
    }
}
