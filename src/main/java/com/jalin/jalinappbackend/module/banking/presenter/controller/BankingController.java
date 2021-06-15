package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.repository.model.TransactionAggregation;
import com.jalin.jalinappbackend.module.banking.service.BankingService;
import com.jalin.jalinappbackend.module.banking.service.TransactionService;
import com.jalin.jalinappbackend.module.banking.service.model.FundTransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("api/v1/banking")
public class BankingController {
    @Autowired
    private BankingService bankingService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts/balance")
    public ResponseEntity<Object> getAccountBalance() {
        BigDecimal accountBalance = bankingService.getAccountBalance();
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }

    @GetMapping("/accounts/transactions")
    public ResponseEntity<Object> getAccountTransactions() {
        Set<TransactionDto> transactionDto = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @GetMapping("/accounts/transactions/most")
    public ResponseEntity<Object> getAccountMostTransactions() {
        Set<TransactionAggregation> transactionDto = transactionService.getMostFrequentTransactions();
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @PostMapping("/transfers")
    public ResponseEntity<Object> fundTransfer(@Valid @RequestBody FundTransferRequest requestBody) {
        bankingService.fundTransfer(requestBody.getBeneficiaryAccountNumber(), requestBody.getAmount());
        return new ResponseEntity<>(
                new SuccessResponse(true, "Fund successfully transferred"),
                HttpStatus.CREATED);
    }
}