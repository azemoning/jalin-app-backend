package com.jalin.jalinappbackend.module.banking.service.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrepaidOption {
    private UUID prepaidId;
    private String prepaidType;
    private String prepaidName;
    private BigDecimal price;

    public void setPrice(BigDecimal amount) {
        this.price = amount.setScale(2, RoundingMode.UNNECESSARY);
    }

    @JsonProperty("data")
    private void unpackData(Map<String, Object> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.prepaidId = objectMapper.convertValue(data.get("prepaidId"), UUID.class);
        this.prepaidType = (String) data.get("prepaidType");
        this.prepaidName = (String) data.get("prepaidName");
        this.price = BigDecimal.valueOf((Double) data.get("price")).setScale(2, RoundingMode.UNNECESSARY);
    }
}
