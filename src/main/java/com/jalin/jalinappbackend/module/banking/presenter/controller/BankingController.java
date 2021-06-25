package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.module.banking.model.ConfirmTransferDto;
import com.jalin.jalinappbackend.module.banking.model.CorporateDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.model.TransferListDto;
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
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/banking")
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
        BigDecimal accountBalance = bankingService.getAccountBalance();
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }

    @GetMapping("/accounts/transactions")
    public ResponseEntity<Object> getAccountTransactions() {
        List<TransactionDto> transactionDto = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @GetMapping("/accounts/transactions/most")
    public ResponseEntity<Object> getAccountMostTransactions() {
        List<TransactionAggregation> transactionDto = transactionService.getMostFrequentTransactions();
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @GetMapping("/transfers")
    public ResponseEntity<Object> getTransferList() {
        List<TransferListDto> transferListDto = transferListService.getTransferList();
        return new ResponseEntity<>(transferListDto, HttpStatus.OK);
    }

    @PostMapping(
            path = "/transfers/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> confirmTransfer(@Valid @ModelAttribute ConfirmTransferRequest requestBody) {
        ConfirmTransferDto confirmTransferDto = bankingService.confirmTransfer(
                requestBody.getCorporateId(), requestBody.getAccountNumber(), requestBody.getAmount());
        return new ResponseEntity<>(confirmTransferDto, HttpStatus.OK);
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
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/transfers/add",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> addTransferList(@Valid @ModelAttribute AddTransferListRequest requestBody) {
        TransferListDto transferListDto = transferListService.addTransferList(requestBody.getCorporateId(), requestBody.getAccountNumber());
        return new ResponseEntity<>(transferListDto, HttpStatus.CREATED);
    }

    @GetMapping("/transfers/corporates/bank")
    public ResponseEntity<Object> getBankCorporates() {
        List<CorporateDto> corporateDtoList = corporateService.getBankCorporates();
        return new ResponseEntity<>(corporateDtoList, HttpStatus.OK);
    }
}
