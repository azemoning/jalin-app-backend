package com.jalin.jalinappbackend.module.banking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrepaidElectricityDto {
    private String customerId;
    private String customerName;
    private List<PrepaidDto> tokenList;
}
