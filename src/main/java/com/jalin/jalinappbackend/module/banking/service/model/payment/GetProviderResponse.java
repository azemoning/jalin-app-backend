package com.jalin.jalinappbackend.module.banking.service.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetProviderResponse {
    private String providerId;
    private String prefixNumber;
    private String productName;
    private String providerName;

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        this.providerId = (String) (data.get("providerId"));
        this.prefixNumber = (String) data.get("prefixNumber");
        this.productName = (String) data.get("productName");
        this.providerName = (String) data.get("providerName");
    }
}
