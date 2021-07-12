package com.jalin.jalinappbackend.module.dashboard.model;

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
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalEntries;
    private Integer currentPage;
    private Integer totalPages;
    private List<TransactionDto> transactionList;
}
