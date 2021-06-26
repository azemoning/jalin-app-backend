package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.TransferListDto;

import java.util.List;

public interface TransferListService {
    List<TransferListDto> getTransferList();
    TransferListDto addTransferList(String corporateId, String beneficiaryAccountNumber);
}
