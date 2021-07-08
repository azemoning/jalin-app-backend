package com.jalin.jalinappbackend.module.gamification.leaderboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListPointRankDto {
    private UUID userId;
    private String fullName;
    private Integer totalPoints;
    private BigInteger rank;
}
