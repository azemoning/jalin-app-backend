package com.jalin.jalinappbackend.module.authentication.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddNewBankAccountRequest {
    private String currency;
    private BigDecimal balance;
}
