package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.model.transaction.TransactionAllDto;
import com.jalin.jalinappbackend.module.dashboard.model.transaction.TransactionDetailsDto;
import com.jalin.jalinappbackend.module.dashboard.model.transaction.TransactionMostFrequentDto;
import com.jalin.jalinappbackend.module.dashboard.service.TransactionDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/v1")
public class TransactionDashboardController {
    @Autowired
    private TransactionDashboardService transactionDashboardService;

    @GetMapping("/transactions")
    public ResponseEntity<Object> getAllTransactions(
            @RequestParam(defaultValue = "C,D") String[] type,
            @RequestParam(defaultValue = "TRANSFER, TRANSFER_DOMESTIC, TOP_UP, PAYMENT_QR") String[] name,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            @RequestParam(defaultValue = "transactionId,desc") String[] sort,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "") String keyword) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        TransactionAllDto transactionAllDto = transactionDashboardService
                .getAllTransactions(type, name, page, size, sort, startDate, endDate, keyword);
        return new ResponseEntity<>(transactionAllDto, HttpStatus.OK);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Object> getTransactionByTransactionId(@PathVariable String transactionId) {
        TransactionDetailsDto transactionDetailsDto = transactionDashboardService.getTransactionById(transactionId);
        return new ResponseEntity<>(transactionDetailsDto, HttpStatus.OK);
    }

    @GetMapping("/transactions/most")
    public ResponseEntity<Object> getMostFrequentTransactions() {
        TransactionMostFrequentDto transactionMostFrequentDto = transactionDashboardService.getMostFrequentTransactions();
        return new ResponseEntity<>(transactionMostFrequentDto, HttpStatus.OK);
    }
}
