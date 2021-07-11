package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.model.TransactionDetailsDto;
import com.jalin.jalinappbackend.module.dashboard.model.TransactionDto;
import com.jalin.jalinappbackend.module.dashboard.service.TransactionDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/v1")
public class TransactionDashboardController {
    @Autowired
    private TransactionDashboardService transactionDashboardService;

    @GetMapping("/transactions")
    public ResponseEntity<Object> getAllTransactions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size) {
        List<TransactionDto> transactionDtoList = transactionDashboardService.getAllTransactions(page, size);
        return new ResponseEntity<>(transactionDtoList, HttpStatus.OK);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Object> getTransactionByTransactionId(@PathVariable String transactionId) {
        TransactionDetailsDto transactionDetailsDto = transactionDashboardService.getTransactionById(transactionId);
        return new ResponseEntity<>(transactionDetailsDto, HttpStatus.OK);
    }
}
