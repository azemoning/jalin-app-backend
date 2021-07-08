package com.jalin.jalinappbackend.module.gamification.voucher.service;

import com.jalin.jalinappbackend.exception.RedeemVoucherFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import com.jalin.jalinappbackend.module.gamification.voucher.entity.UserVoucher;
import com.jalin.jalinappbackend.module.gamification.voucher.entity.Voucher;
import com.jalin.jalinappbackend.module.gamification.voucher.model.UserVoucherDto;
import com.jalin.jalinappbackend.module.gamification.voucher.repository.UserVoucherRepository;
import com.jalin.jalinappbackend.module.gamification.voucher.repository.VoucherRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserVoucherServiceImpl implements UserVoucherService {

    @Autowired
    private UserVoucherRepository userVoucherRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    @Override
    public List<UserVoucherDto> getUserVouchers() {
        User user = getSignedInUser();
        List<UserVoucher> userVoucherList = userVoucherRepository
                .findUserVouchersByUserAndIsActiveEquals(user, true);

        List<UserVoucherDto> userVoucherDtoList = new ArrayList<>();

        for (UserVoucher userVoucher : userVoucherList) {
            Voucher voucher = voucherRepository.findById(userVoucher.getVoucher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
            UserVoucherDto userVoucherDto = modelMapperUtility.initialize().map(userVoucher, UserVoucherDto.class);

            userVoucherDto.setCategory(voucher.getCategory());
            userVoucherDto.setPoints(voucher.getPoints());
            userVoucherDto.setUsage(voucher.getUsage());
            userVoucherDto.setTncDescription(voucher.getTncDescription());
            userVoucherDto.setValidity(voucher.getValidity());

            userVoucherDtoList.add(userVoucherDto);
        }

        return userVoucherDtoList;
    }

    @Override
    public void redeemVoucher(UUID voucherId) {
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalDate today = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));

        User user = getSignedInUser();
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        List<UserVoucher> userVouchers = userVoucherRepository
                .findUserVouchersByUserAndIsActiveEquals(user, true);

        UserVoucher newUserVoucher = new UserVoucher();

        Point userPoint = pointRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User point not found"));


        // if user's point < voucher point fee
        if (userPoint.getTotalPoints() < voucher.getPoints()) {
            throw new RedeemVoucherFailedException("Insufficient point");
        }

        // if user's voucher already redeemed
        for (UserVoucher userVoucher : userVouchers) {
            if (userVoucher.getVoucher().equals(voucher) && userVoucher.getIsActive().equals(true)) {
                throw new RedeemVoucherFailedException("Voucher already redeemed");
            }
        }

        newUserVoucher.setUser(user);
        newUserVoucher.setVoucher(voucher);
        newUserVoucher.setIsActive(true);
        newUserVoucher.setRemaining(voucher.getQuota());
        newUserVoucher.setRedeemedAt(today);

        userVoucherRepository.save(newUserVoucher);
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?", zone = "GMT+7.00")
    public void checkUserVoucherExpiration() {
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalDate today = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));

        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<UserVoucher> userVouchers = userVoucherRepository
                    .findUserVouchersByUserAndIsActiveEquals(user, true);

            for (UserVoucher userVoucher : userVouchers) {
                if (userVoucher.getVoucher().getValidity().compareTo(today) < 0) {
                    userVoucher.setIsActive(false);
                    userVoucherRepository.save(userVoucher);
                }
            }
        }
    }

    @Override
    public void applyVoucher() {

    }

    @Override
    public void simulateApplyVoucher(UUID userVoucherId) {
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalDate today = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));

        User user = getSignedInUser();
        UserVoucher userVoucher = userVoucherRepository
                .findUserVoucherByIdAndUserAndIsActiveEquals(userVoucherId, user, true)
                .orElseThrow(() -> new ResourceNotFoundException("User voucher not found"));

        userVoucher.setRemaining(userVoucher.getRemaining() - 1);
        userVoucher.setUsedAt(today);

        if (userVoucher.getRemaining() == 0) {
            userVoucher.setIsActive(false);
        }

        userVoucherRepository.save(userVoucher);

    }

    @Override
    public List<UserVoucherDto> getUserVoucherHistories() {
        User user = getSignedInUser();
        List<UserVoucher> userVoucherList = userVoucherRepository
                .findUserVouchersByUserAndIsActiveEquals(user, false);

        List<UserVoucherDto> userVoucherDtoList = new ArrayList<>();

        for (UserVoucher userVoucher : userVoucherList) {
            Voucher voucher = voucherRepository.findById(userVoucher.getVoucher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
            UserVoucherDto userVoucherDto = modelMapperUtility.initialize().map(userVoucher, UserVoucherDto.class);

            userVoucherDto.setCategory(voucher.getCategory());
            userVoucherDto.setPoints(voucher.getPoints());
            userVoucherDto.setUsage(voucher.getUsage());
            userVoucherDto.setTncDescription(voucher.getTncDescription());
            userVoucherDto.setValidity(voucher.getValidity());

            userVoucherDtoList.add(userVoucherDto);
        }

        return userVoucherDtoList;
    }

    @Override
    public void setUserVoucherToExpired(UUID userVoucherId) {
        UserVoucher userVoucher = userVoucherRepository.findById(userVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("User voucher not found"));

        userVoucher.setIsActive(false);
        userVoucherRepository.save(userVoucher);
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
