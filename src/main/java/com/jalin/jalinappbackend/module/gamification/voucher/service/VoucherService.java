package com.jalin.jalinappbackend.module.gamification.voucher.service;

import com.jalin.jalinappbackend.module.gamification.voucher.entity.Voucher;

import java.util.List;
import java.util.UUID;

public interface VoucherService {
    List<Voucher> getAllVouchers();
    Voucher getVoucher(UUID voucherId);
    void addVoucher(Voucher voucher);
    void updateVoucher(UUID voucherId, Voucher voucher);
    void deleteVoucher(UUID voucherId);
}