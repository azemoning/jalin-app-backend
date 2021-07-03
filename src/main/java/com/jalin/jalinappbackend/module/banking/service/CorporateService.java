package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.CorporateDto;

import java.util.List;

public interface CorporateService {
    List<CorporateDto> getBankCorporates();
    List<CorporateDto> getDigitalWalletCorporates();
    List<CorporateDto> getMerchantCorporates();
    CorporateDto getCorporateByCorporateId(String corporateId);
}
