package com.jalin.jalinappbackend.module.gamification.mission.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMissionDto {
    private String activity;
    private String missionDescription;
    private String tncDescription;
    private Integer frequency;
    private Integer minimumAmount;
    private String expiration;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer missionProgress;
    private LocalTime completionTime;
    private String status;
    private Integer point;
}
