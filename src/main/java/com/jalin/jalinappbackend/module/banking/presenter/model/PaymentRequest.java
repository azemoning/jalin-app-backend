package com.jalin.jalinappbackend.module.banking.presenter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequest {
    @NotBlank
    private String corporateId;
    @Positive
    private BigDecimal amount;
    private String transactionNote;

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.UNNECESSARY);
    }
}
