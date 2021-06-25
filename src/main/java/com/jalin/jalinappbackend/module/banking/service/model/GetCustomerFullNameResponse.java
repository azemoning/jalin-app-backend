package com.jalin.jalinappbackend.module.banking.service.model;

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
public class GetCustomerFullNameResponse {
    private String fullName;

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        this.fullName = (String) data.get("fullName");
    }
}
