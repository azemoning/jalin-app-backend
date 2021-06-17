package com.jalin.jalinappbackend.module.authentication.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
    Optional<UserDetails> findByMobileNumber(String mobileNumber);
    Optional<UserDetails> findByAccountNumber(String accountNumber);
    Optional<UserDetails> findByIdCardNumber(String idCardNumber);
    Optional<UserDetails> findByUser(User user);
}
