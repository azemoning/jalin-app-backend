package com.jalin.jalinappbackend.module.banking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class TransactionDto {
    private String transactionName;
    private String corporateId;
    private String corporateName;
    private String accountNumber;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private String transactionType;
    private String currency;
    private String amount;
    private String transactionMessage;
    private String transactionNote;

    public TransactionDto(
            String transactionName,
            String corporateId,
            String corporateName,
            String accountNumber,
            LocalDate transactionDate,
            LocalTime transactionTime,
            String transactionType,
            String currency,
            BigDecimal amount,
            String transactionMessage,
            String transactionNote) {
        this.transactionName = transactionName;
        this.corporateId = corporateId;
        this.corporateName = corporateName;
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
        this.currency = currency;
        this.amount = formatAmount(amount);
        this.transactionMessage = transactionMessage;
        this.transactionNote = transactionNote;
    }

    public void setAmount(String amount) {
        this.amount = formatAmount(new BigDecimal(amount));
    }

    public void setAmount(BigDecimal amount) {
        this.amount = formatAmount(amount);
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
