package com.jalin.jalinappbackend.module.banking.service.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalin.jalinappbackend.module.banking.service.model.TransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentMobilePhoneCreditResponse {
    private TransactionResponse sourceTransaction;

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.sourceTransaction = objectMapper.convertValue(data, TransactionResponse.class);
    }
}
