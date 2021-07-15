package com.jalin.jalinappbackend.module.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeaderboardDataDto {
    private BigInteger rank;
    private String profilePicture;
    private String jalinId;
    private Integer totalPoints;
    private BigInteger missionSolved;
}
