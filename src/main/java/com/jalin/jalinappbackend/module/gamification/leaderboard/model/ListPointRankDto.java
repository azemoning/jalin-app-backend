package com.jalin.jalinappbackend.module.gamification.leaderboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListPointRankDto {
    private BigInteger rank;
    private String jalinId;
    private Integer totalPoints;
}
