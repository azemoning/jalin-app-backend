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
public class ConfirmPaymentDto {
    private String corporateId;
    private String corporateName;
    private String paymentAmount;
    private String paymentFee;
    private String paymentDiscount;
    private String totalPaymentAmount;

    public ConfirmPaymentDto(
            String corporateId,
            String corporateName,
            BigDecimal paymentAmount,
            BigDecimal paymentFee,
            BigDecimal paymentDiscount,
            BigDecimal totalPaymentAmount) {
        this.corporateId = corporateId;
        this.corporateName = corporateName;
        this.paymentAmount = formatAmount(paymentAmount);
        this.paymentFee = formatAmount(paymentFee);
        this.paymentDiscount = formatAmount(paymentDiscount);
        this.totalPaymentAmount = formatAmount(totalPaymentAmount);
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setPaymentFee(String paymentFee) {
        this.paymentFee = paymentFee;
    }

    public void setPaymentDiscount(String paymentDiscount) {
        this.paymentDiscount = paymentDiscount;
    }

    public void setTotalPaymentAmount(String totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = formatAmount(paymentAmount);
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = formatAmount(paymentFee);
    }

    public void setPaymentDiscount(BigDecimal paymentDiscount) {
        this.paymentDiscount = formatAmount(paymentDiscount);
    }

    public void setTotalPaymentAmount(BigDecimal totalPaymentAmount) {
        this.totalPaymentAmount = formatAmount(totalPaymentAmount);
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
