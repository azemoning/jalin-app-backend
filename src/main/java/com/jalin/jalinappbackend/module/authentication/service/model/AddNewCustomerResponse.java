package com.jalin.jalinappbackend.module.authentication.service.model;

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
public class AddNewCustomerResponse {
    private Boolean success;
    private String customerId;

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        this.customerId = (String) data.get("customerId");
    }
}
