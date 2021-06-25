package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.WalletListDto;

import java.util.List;

public interface WalletListService {
    List<WalletListDto> getWalletList();
    WalletListDto addWalletList(String corporateId, String beneficiaryAccountNumber);
}
