package com.jalin.jalinappbackend.module.banking.service.model.corporate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetCorporatesResponse {
    @JsonProperty("data")
    List<CorporateResponse> corporateList;
}
