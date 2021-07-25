package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.module.banking.model.ConfirmTransferDto;
import com.jalin.jalinappbackend.module.banking.model.CorporateDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.model.WalletListDto;
import com.jalin.jalinappbackend.module.banking.presenter.model.AddWalletListRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.ConfirmTransferRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.FundTransferRequest;
import com.jalin.jalinappbackend.module.banking.service.CorporateService;
import com.jalin.jalinappbackend.module.banking.service.TopUpService;
import com.jalin.jalinappbackend.module.banking.service.WalletListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${url.map.api}/v1/banking")
public class TopUpController {
    @Autowired
    private CorporateService corporateService;
    @Autowired
    private TopUpService topUpService;
    @Autowired
    private WalletListService walletListService;

    @GetMapping("/transfers/virtual")
    public ResponseEntity<Object> getWalletList() {
        List<WalletListDto> walletListDto = walletListService.getWalletList();
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Wallet list successfully found", walletListDto),
                HttpStatus.OK);
    }

    @PostMapping(
            path = "/transfers/virtual",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> fundTransferVirtual(@Valid @ModelAttribute FundTransferRequest requestBody) {
        TransactionDto transactionDto = topUpService.fundTransferVirtual(
                requestBody.getCorporateId(),
                requestBody.getBeneficiaryAccountNumber(),
                requestBody.getAmount(),
                requestBody.getTransactionNote());
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/transfers/virtual/add",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> addWalletList(@Valid @ModelAttribute AddWalletListRequest requestBody) {
        WalletListDto walletListDto = walletListService.addWalletList(requestBody.getCorporateId(), requestBody.getAccountNumber());
        return new ResponseEntity<>(walletListDto, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/transfers/virtual/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> confirmTransferVirtual(@Valid @ModelAttribute ConfirmTransferRequest requestBody) {
        ConfirmTransferDto confirmTransferDto = topUpService.confirmTransfer(
                requestBody.getCorporateId(), requestBody.getAccountNumber(), requestBody.getAmount());
        return new ResponseEntity<>(confirmTransferDto, HttpStatus.OK);
    }

    @GetMapping("/transfers/corporates/wallet")
    public ResponseEntity<Object> getDigitalWalletCorporates() {
        List<CorporateDto> corporateDtoList = corporateService.getDigitalWalletCorporates();
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Wallet successfully found", corporateDtoList),
                HttpStatus.OK);
    }
}
