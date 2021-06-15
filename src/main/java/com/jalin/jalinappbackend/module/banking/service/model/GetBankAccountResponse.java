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
public class GetBankAccountResponse {
    private BigDecimal balance;

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        this.balance = BigDecimal.valueOf((Double) data.get("balance")).setScale(2, RoundingMode.UNNECESSARY);
    }
}
