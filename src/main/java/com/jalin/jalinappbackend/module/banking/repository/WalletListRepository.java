package com.jalin.jalinappbackend.module.banking.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.banking.entity.WalletList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletListRepository extends JpaRepository<WalletList, UUID> {
    List<WalletList> findByUser(User user);
    Optional<WalletList> findByUserAndCorporateIdAndAccountNumber(User user, String corporateId, String accountNumber);
}
