package com.jalin.jalinappbackend.module.banking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BalanceDto {
    private String balance;

    public BalanceDto(BigDecimal balance) {
        this.balance = formatAmount(balance);
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = formatAmount(balance);
    }

    private String formatAmount(BigDecimal amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        String pattern = "#,###,###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        return decimalFormat.format(amount);
    }
}
