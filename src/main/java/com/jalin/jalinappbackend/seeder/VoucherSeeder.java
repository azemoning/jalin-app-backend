package com.jalin.jalinappbackend.seeder;

import com.jalin.jalinappbackend.module.gamification.voucher.entity.Voucher;
import com.jalin.jalinappbackend.module.gamification.voucher.repository.VoucherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Order(value = 4)
@Slf4j
public class VoucherSeeder implements CommandLineRunner {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public void run(String... args) {
        try {
            seed();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private void seed() {
        if (voucherRepository.count() == 0) {
            ZoneId zoneId = ZoneId.of("Asia/Jakarta");
            ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
            LocalDate today = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));

            addVoucher(
                    "JALIN_OFFER",
                    "TRANSFER_DOMESTIC",
                    0,
                    10,
                    "Free 10x transfer to any bank",
                    7000,
                    today.plusMonths(6)
            );

            addVoucher(
                    "JALIN_OFFER",
                    "TRANSFER_DOMESTIC",
                    0,
                    3,
                    "Free 3x transfer to any bank voucher",
                    1500,
                    today.plusMonths(3)
            );

            addVoucher(
                    "JALIN_OFFER",
                    "TRANSFER_DOMESTIC",
                    0,
                    5,
                    "Free 5x transfer to any bank voucher",
                    2000,
                    today.plusMonths(3)
            );

            addVoucher(
                    "JALIN_OFFER",
                    "TOP_UP",
                    0,
                    10,
                    "Free 10x top up e-wallet (OVO, GOPAY, Shopee Pay)",
                    750,
                    today.plusMonths(3)
            );

            addVoucher(
                    "JALIN_OFFER",
                    "PAYMENT_BILL_PLN",
                    0,
                    3,
                    "Free 3x PLN bill payment fee",
                    1000,
                    today.plusMonths(6)
            );

            addVoucher(
                    "JALIN_OFFER",
                    "PAYMENT_BILL_PDAM",
                    0,
                    3,
                    "Free 3x PDAM bill payment fee",
                    1000,
                    today.plusMonths(6)
            );

            addVoucher(
                    "JALIN_OFFER",
                    "PAYMENT_BILL_INTERNET",
                    0,
                    3,
                    "Free 3x Indihome or FirstMedia or Biznet bill payment fee",
                    1000,
                    today.plusMonths(6)
            );

            addVoucher(
                    "VOUCHER",
                    "PAYMENT_MOBILE_PHONE_CREDIT",
                    25000,
                    1,
                    "Free pulsa any operator Rp 25.000",
                    4500,
                    today.plusMonths(3)
            );

            addVoucher(
                    "VOUCHER",
                    "PAYMENT_MOBILE_PHONE_CREDIT",
                    15000,
                    1,
                    "Free pulsa any operator Rp 15.000",
                    3000,
                    today.plusMonths(3)
            );

            addVoucher(
                    "VOUCHER",
                    "PAYMENT_MOBILE_PHONE_DATA",
                    20000,
                    1,
                    "Discount Tsel or Indosat or SmartFren, or Three quota Rp 20.000",
                    5000,
                    today.plusMonths(3)
            );
        }
    }

    private void addVoucher(
            String category,
            String usage,
            Integer amount,
            Integer quota,
            String tncDescription,
            Integer points,
            LocalDate validity
    ) {
        Voucher voucher = new Voucher();
        voucher.setCategory(category);
        voucher.setUsage(usage);
        voucher.setAmount(amount);
        voucher.setQuota(quota);
        voucher.setTncDescription(tncDescription);
        voucher.setPoints(points);
        voucher.setValidity(validity);

        voucherRepository.save(voucher);
    }
}
