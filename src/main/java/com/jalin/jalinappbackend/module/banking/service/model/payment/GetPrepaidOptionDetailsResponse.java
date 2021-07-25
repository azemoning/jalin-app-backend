package com.jalin.jalinappbackend.module.banking.service.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetPrepaidOptionDetailsResponse {
    private List<PrepaidOptionDetails> prepaidOptionDetailsList;

    @JsonProperty("data")
    private void unpackData(List<Map<String, Object>> dataList) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<PrepaidOptionDetails> prepaidOptionDetailsList = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            PrepaidOptionDetails prepaidOptionDetails = objectMapper.convertValue(data, PrepaidOptionDetails.class);
            prepaidOptionDetailsList.add(prepaidOptionDetails);
        }
        this.prepaidOptionDetailsList = prepaidOptionDetailsList;
    }
}
