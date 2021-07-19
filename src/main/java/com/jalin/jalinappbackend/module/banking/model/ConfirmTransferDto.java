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
public class ConfirmTransferDto {
    private String corporateId;
    private String corporateName;
    private String accountNumber;
    private String fullName;
    private String transferAmount;
    private String transferFee;
    private String totalTransferAmount;

    public ConfirmTransferDto(
            String corporateId,
            String corporateName,
            String accountNumber,
            String fullName,
            BigDecimal transferAmount,
            BigDecimal transferFee,
            BigDecimal totalTransferAmount) {
        this.corporateId = corporateId;
        this.corporateName = corporateName;
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.transferAmount = formatAmount(transferAmount);
        this.transferFee = formatAmount(transferFee);
        this.totalTransferAmount = formatAmount(totalTransferAmount);
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public void setTotalTransferAmount(String totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = formatAmount(transferAmount);
    }

    public void setTransferFee(BigDecimal transferFee) {
        this.transferFee = formatAmount(transferFee);
    }

    public void setTotalTransferAmount(BigDecimal totalTransferAmount) {
        this.totalTransferAmount = formatAmount(totalTransferAmount);
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
