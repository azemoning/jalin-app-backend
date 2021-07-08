package com.jalin.jalinappbackend.module.gamification.voucher.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.voucher.service.UserVoucherService;
import com.jalin.jalinappbackend.module.gamification.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class UserVoucherController {

    @Autowired
    private UserVoucherService userVoucherService;

    @Autowired
    private VoucherService voucherService;

    @GetMapping("voucher/allAvailable")
    public ResponseEntity<Object> getAllAvailableVouchers() {
        return new ResponseEntity<>(voucherService.getAllVouchers(), HttpStatus.OK);
    }

    @GetMapping("voucher")
    public ResponseEntity<Object> getUserVouchers() {
        return new ResponseEntity<>(userVoucherService.getUserVouchers(), HttpStatus.OK);
    }

    @PostMapping("voucher/{voucherId}")
    public ResponseEntity<Object> redeemVoucher(@PathVariable UUID voucherId) {
        userVoucherService.redeemVoucher(voucherId);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Voucher redeemed successfully"),
                HttpStatus.OK
        );
    }

    @PostMapping("voucher/simulateApplyVoucher/{userVoucherId}")
    public ResponseEntity<Object> simulateApplyVoucher(@PathVariable UUID userVoucherId) {
        userVoucherService.simulateApplyVoucher(userVoucherId);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Voucher applied successfully"),
                HttpStatus.OK
        );
    }

    @PostMapping("voucher/setUserVoucherToExpired/{userVoucherId}")
    public ResponseEntity<Object> setUserVoucherToExpired(@PathVariable UUID userVoucherId) {
        userVoucherService.setUserVoucherToExpired(userVoucherId);
        return new ResponseEntity<>(
                new SuccessResponse(true, "User voucher set to expired"),
                HttpStatus.OK
        );
    }

    @GetMapping("voucher/userVoucherHistories")
    public ResponseEntity<Object> getUserVoucherHistories() {
        return new ResponseEntity<>(userVoucherService.getUserVoucherHistories(), HttpStatus.OK);
    }

}
