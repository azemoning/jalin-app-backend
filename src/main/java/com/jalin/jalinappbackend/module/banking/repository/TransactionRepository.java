package com.jalin.jalinappbackend.module.banking.repository;

import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
