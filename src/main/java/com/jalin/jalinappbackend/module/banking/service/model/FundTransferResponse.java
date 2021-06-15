package com.jalin.jalinappbackend.module.banking.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundTransferResponse {
    private TransactionResponse sourceTransaction;
    private TransactionResponse beneficiaryTransaction;

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.sourceTransaction = objectMapper.convertValue(data.get("sourceTransaction"), TransactionResponse.class);
        this.beneficiaryTransaction = objectMapper.convertValue(data.get("beneficiaryTransaction"), TransactionResponse.class);
    }
}
