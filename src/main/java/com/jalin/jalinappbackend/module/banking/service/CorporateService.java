package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.CorporateDto;

import java.util.List;

public interface CorporateService {
    List<CorporateDto> getBankCorporates();
    CorporateDto getCorporateByCorporateId(String corporateId);
}
