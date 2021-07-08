package com.jalin.jalinappbackend.module.gamification.voucher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserVoucherDto {
    private UUID id;
    private LocalDate redeemedAt;
    private LocalDate usedAt;
    private String category;
    private String usage;
    private Integer remaining;
    private String tncDescription;
    private LocalDate validity;
    private Integer points;
    private Boolean isActive;
}
