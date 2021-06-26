package com.jalin.jalinappbackend.module.banking.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.repository.model.TransactionAggregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByUserOrderByCreatedDateAsc(User user);
    @Query("SELECT new com.jalin.jalinappbackend.module.banking.repository.model.TransactionAggregation(" +
            "t.transactionName, t.corporateId, t.accountNumber, COUNT(*)) " +
            "FROM Transaction t " +
            "WHERE t.user = :user " +
            "AND t.transactionType = 'C' " +
            "GROUP BY t.transactionName, t.corporateId, t.accountNumber " +
            "ORDER BY COUNT(*) DESC")
    List<TransactionAggregation> findMostFrequentTransactions(User user);

    Transaction findTopByUserAndTransactionTypeEqualsOrderByCreatedDateDesc(User user, String transactionType);
}
