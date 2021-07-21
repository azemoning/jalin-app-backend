package com.jalin.jalinappbackend.module.gamification.voucher.service;

import com.jalin.jalinappbackend.exception.AddMissionFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.gamification.voucher.entity.UserVoucher;
import com.jalin.jalinappbackend.module.gamification.voucher.entity.Voucher;
import com.jalin.jalinappbackend.module.gamification.voucher.repository.UserVoucherRepository;
import com.jalin.jalinappbackend.module.gamification.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private UserVoucherRepository userVoucherRepository;

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher getVoucher(UUID voucherId) {
        return voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
    }

    @Override
    public void addVoucher(Voucher voucher) {
        List<Voucher> voucherList = voucherRepository.findAll();

        for (Voucher data : voucherList) {
            if (voucher.getCategory().equals(data.getCategory())) {
                if (voucher.getUsage().equals(data.getUsage())) {
                    if (voucher.getAmount().equals(data.getAmount())) {
                        if (voucher.getQuota().compareTo(data.getQuota()) == 0) {
                            if (voucher.getPoints().compareTo(data.getPoints()) == 0) {
                                if (voucher.getValidity().equals(data.getValidity())) {
                                    throw new AddMissionFailedException("Voucher with same details are already exists");
                                }
                            }
                        }
                    }
                }
            }
        }

        voucherRepository.save(voucher);
    }

    @Override
    public void updateVoucher(UUID voucherId, Voucher voucher) {
        Voucher findVoucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        findVoucher.setCategory(voucher.getCategory());
        findVoucher.setTncDescription(voucher.getTncDescription());
        findVoucher.setPoints(voucher.getPoints());
        findVoucher.setAmount(voucher.getAmount());
        findVoucher.setQuota(voucher.getQuota());
        findVoucher.setStatus(voucher.getStatus());
        findVoucher.setUsage(voucher.getUsage());
        findVoucher.setValidity(voucher.getValidity());

        voucherRepository.save(findVoucher);
    }

    @Override
    public void deleteVoucher(UUID voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        UserVoucher userVoucher = userVoucherRepository.findUserVoucherByVoucher(voucher);

        if (userVoucher != null) {
            userVoucherRepository.delete(userVoucher);
        }

        voucherRepository.delete(voucher);
    }
}
