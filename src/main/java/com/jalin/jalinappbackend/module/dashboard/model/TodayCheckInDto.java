package com.jalin.jalinappbackend.module.dashboard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@Getter
@Setter
public class TodayCheckInDto {
    private Integer totalUsers;
    private Integer totalCheckIn;
    private BigDecimal checkInPercentage;

    public TodayCheckInDto(Integer totalUsers, Integer totalCheckIn) {
        this.totalUsers = totalUsers;
        this.totalCheckIn = totalCheckIn;
        this.checkInPercentage = BigDecimal.valueOf((float) totalCheckIn / (float) totalUsers * 100)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
