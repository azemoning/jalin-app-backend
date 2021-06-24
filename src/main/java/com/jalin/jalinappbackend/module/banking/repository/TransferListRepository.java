package com.jalin.jalinappbackend.module.banking.repository;

import com.jalin.jalinappbackend.module.banking.entity.TransferList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferListRepository extends JpaRepository<TransferList, UUID> {
}
