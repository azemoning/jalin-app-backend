package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.voucher.entity.Voucher;
import com.jalin.jalinappbackend.module.gamification.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping("voucher")
    public ResponseEntity<Object> getAllVouchers() {
        return new ResponseEntity<>(voucherService.getAllVouchers(), HttpStatus.OK);
    }

    @GetMapping("voucher/{voucherId}")
    public ResponseEntity<Object> getVoucherById(@PathVariable UUID voucherId) {
        return new ResponseEntity<>(voucherService.getVoucher(voucherId), HttpStatus.OK);
    }

    @PostMapping("voucher")
    public ResponseEntity<Object> addNewVoucher(@RequestBody Voucher voucher) {
        voucherService.addVoucher(voucher);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Voucher added successfully"),
                HttpStatus.CREATED
        );
    }

    @PutMapping("voucher/{voucherId}")
    public ResponseEntity<Object> updateVoucher(
            @PathVariable UUID voucherId,
            @RequestBody Voucher voucher
    ) {
        voucherService.updateVoucher(voucherId, voucher);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Voucher updated successfully"),
                HttpStatus.OK
        );
    }

    @DeleteMapping("voucher/{voucherId}")
    public ResponseEntity<Object> deleteVoucher(@PathVariable UUID voucherId) {
        voucherService.deleteVoucher(voucherId);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Voucher deleted successfully"),
                HttpStatus.OK
        );
    }

}
