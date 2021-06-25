package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.module.banking.model.TransferListDto;

public interface TransferListService {
    TransferListDto addTransferList(String corporateId, String beneficiaryAccountNumber);
}
