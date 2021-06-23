package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.CorporateDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface BankingService {
    BigDecimal getAccountBalance();
    void fundTransfer(String beneficiaryAccountNumber, BigDecimal amount);
    TransactionDto fundTransferDomestic(String corporateId, String beneficiaryAccountNumber, BigDecimal amount);
    List<CorporateDto> getBankCorporates();
}
