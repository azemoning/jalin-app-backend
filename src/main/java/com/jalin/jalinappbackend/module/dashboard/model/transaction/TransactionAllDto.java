package com.jalin.jalinappbackend.module.dashboard.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionAllDto {
    private List<String> transactionType;
    private List<String> transactionName;
    private Long totalEntries;
    private Integer currentPage;
    private Integer totalPages;
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
    private List<TransactionDto> transactionList;
}
