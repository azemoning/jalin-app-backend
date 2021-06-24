package com.jalin.jalinappbackend.module.gamification.mission.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMissionDto {
    private UUID id;
    private String activity;
    private String missionDescription;
    private String tncDescription;
    private Integer frequency;
    private BigDecimal minimumAmount;
    private String expiration;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer missionProgress;
    private LocalTime completionTime;
    private String status;
    private Boolean isClaimed;
    private Boolean isActive;
    private Integer point;
}
