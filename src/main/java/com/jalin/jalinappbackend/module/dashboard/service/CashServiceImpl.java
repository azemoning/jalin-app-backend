package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.dashboard.model.CashDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class CashServiceImpl implements CashService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public CashDto getCash() {
        List<String> label = new ArrayList<>();
        List<BigDecimal> cashIn = new ArrayList<>();
        List<BigDecimal> cashOut = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int day = 0; day <= 6; day++) {
            LocalDate transactionDate = LocalDate.parse(dateFormat.format(calendar.getTime()));
            List<Transaction> transactionListDebit = transactionRepository
                    .findByTransactionTypeAndTransactionDate("D", transactionDate);
            List<Transaction> transactionListCredit = transactionRepository
                    .findByTransactionTypeAndTransactionDate("C", transactionDate);

            label.add(String.format("Day %s", day + 1));
            cashIn.add(calculateCash(transactionListDebit));
            cashOut.add(calculateCash(transactionListCredit));

            calendar.add(Calendar.DATE, 1);
        }

        CashDto cashDto = new CashDto();
        cashDto.setLabel(label);
        cashDto.setCashIn(cashIn);
        cashDto.setCashOut(cashOut);
        return cashDto;
    }

    private BigDecimal calculateCash(List<Transaction> transactionList) {
        BigDecimal totalCash = new BigDecimal("0");

        if (transactionList.isEmpty()) {
            return totalCash;
        }

        for (Transaction transaction : transactionList) {
            BigDecimal transactionAmount = transaction.getAmount();
            totalCash = totalCash.add(transactionAmount);
        }

        return totalCash;
    }
}
