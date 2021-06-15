package com.jalin.jalinappbackend.module.gamification.checkin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckInDto {
    private Boolean isCheckedIn;
    private Integer counter;
    private Instant lastCheckedIn;
}
