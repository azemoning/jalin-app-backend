package com.jalin.jalinappbackend.module.gamification.voucher.service;

import com.jalin.jalinappbackend.module.gamification.voucher.model.UserVoucherDto;

import java.util.List;
import java.util.UUID;

public interface UserVoucherService {
    List<UserVoucherDto> getUserVouchers();
    void redeemVoucher(UUID voucherId);
    void checkUserVoucherExpiration();
    void applyVoucher();
    void simulateApplyVoucher(UUID userVoucherId);
    List<UserVoucherDto> getUserVoucherHistories();
    void setUserVoucherToExpired(UUID userVoucherId);
}
