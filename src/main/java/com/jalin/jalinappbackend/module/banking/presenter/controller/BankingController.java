package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.module.banking.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/banking")
public class BankingController {
    @Autowired
    private BankingService bankingService;

    @GetMapping("/accounts/balance")
    public ResponseEntity<Object> getAccountBalance() {
        BigDecimal accountBalance = bankingService.getAccountBalance();
        return new ResponseEntity<>(accountBalance, HttpStatus.OK);
    }
}
