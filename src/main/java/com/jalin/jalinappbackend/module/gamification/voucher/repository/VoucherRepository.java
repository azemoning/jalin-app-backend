package com.jalin.jalinappbackend.module.gamification.voucher.repository;

import com.jalin.jalinappbackend.module.gamification.voucher.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    List<Voucher> findVouchersByStatusEquals(Boolean status);
}
