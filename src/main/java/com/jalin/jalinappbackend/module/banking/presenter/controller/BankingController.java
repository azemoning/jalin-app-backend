package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.module.banking.model.*;
import com.jalin.jalinappbackend.module.banking.presenter.model.AddTransferListRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.ConfirmTransferRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.FundTransferRequest;
import com.jalin.jalinappbackend.module.banking.repository.model.TransactionAggregation;
import com.jalin.jalinappbackend.module.banking.service.BankingService;
import com.jalin.jalinappbackend.module.banking.service.CorporateService;
import com.jalin.jalinappbackend.module.banking.service.TransactionService;
import com.jalin.jalinappbackend.module.banking.service.TransferListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${url.map.api}/v1/banking")
public class BankingController {
    @Autowired
    private BankingService bankingService;
    @Autowired
    private CorporateService corporateService;
    @Autowired
    private TransferListService transferListService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts/balance")
    public ResponseEntity<Object> getAccountBalance() {
        BalanceDto balanceDto = bankingService.getAccountBalance();
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Account balance successfully found", balanceDto),
                HttpStatus.OK);
    }

    @GetMapping("/accounts/transactions")
    public ResponseEntity<Object> getAccountTransactions() {
        List<TransactionDto> transactionDtoList = transactionService.getAllTransactions();
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Account transactions successfully found", transactionDtoList),
                HttpStatus.OK);
    }

    @GetMapping("/accounts/transactions/most")
    public ResponseEntity<Object> getAccountMostTransactions() {
        List<TransactionAggregation> transactionAggregationList = transactionService.getMostFrequentTransactions();
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Account most transactions successfully found", transactionAggregationList),
                HttpStatus.OK);
    }

    @GetMapping("/transfers")
    public ResponseEntity<Object> getTransferList() {
        List<TransferListDto> transferListDto = transferListService.getTransferList();
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Transfer list successfully found", transferListDto),
                HttpStatus.OK);
    }

    @PostMapping(
            path = "/transfers/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> confirmTransfer(@Valid @ModelAttribute ConfirmTransferRequest requestBody) {
        ConfirmTransferDto confirmTransferDto = bankingService.confirmTransfer(
                requestBody.getCorporateId(), requestBody.getAccountNumber(), requestBody.getAmount());
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Please confirm the transfer details", confirmTransferDto),
                HttpStatus.OK);
    }

    @PostMapping(
            path = "/transfers",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> fundTransfer(@Valid @ModelAttribute FundTransferRequest requestBody) {
        TransactionDto transactionDto;
        if (!requestBody.getCorporateId().equals("212")) {
            transactionDto = bankingService.fundTransferDomestic(
                    requestBody.getCorporateId(),
                    requestBody.getBeneficiaryAccountNumber(),
                    requestBody.getAmount(),
                    requestBody.getTransactionNote());
        } else {
            transactionDto = bankingService.fundTransfer(
                    requestBody.getBeneficiaryAccountNumber(),
                    requestBody.getAmount(),
                    requestBody.getTransactionNote());
        }
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Fund successfully transferred", transactionDto),
                HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/transfers/add",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> addTransferList(@Valid @ModelAttribute AddTransferListRequest requestBody) {
        TransferListDto transferListDto = transferListService.addTransferList(requestBody.getCorporateId(), requestBody.getAccountNumber());
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Transfer list successfully added", transferListDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/transfers/corporates/bank")
    public ResponseEntity<Object> getBankCorporates() {
        List<CorporateDto> corporateDtoList = corporateService.getBankCorporates();
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Bank corporates successfully found", corporateDtoList),
                HttpStatus.OK);
    }
}
