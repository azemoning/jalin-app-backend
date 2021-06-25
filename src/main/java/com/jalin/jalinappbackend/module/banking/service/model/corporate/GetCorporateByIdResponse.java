package com.jalin.jalinappbackend.module.banking.service.model.corporate;

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
public class GetCorporateByIdResponse {
    private String corporateId;
    private String corporateName;
    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        this.corporateId = (String) data.get("corporateId");
        this.corporateName = (String) data.get("corporateName");
    }
}
