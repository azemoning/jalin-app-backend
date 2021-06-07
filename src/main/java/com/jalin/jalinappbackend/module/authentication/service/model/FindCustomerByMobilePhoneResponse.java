package com.jalin.jalinappbackend.module.authentication.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindCustomerByMobilePhoneResponse {
    private Boolean success;
    private String fullName;
    private String mobileNumber;
    private String accountNumber;

    @SuppressWarnings("unchecked")
    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        this.fullName = (String)data.get("fullName");
        this.mobileNumber = (String)data.get("mobileNumber");

        List<Object> objectList = (List<Object>)data.get("accounts");
        Map<String, Object> object = new ObjectMapper()
                .convertValue(objectList.stream().findFirst().get(), Map.class);
        this.accountNumber = (String)object.get("accountNumber");
    }
}
