package com.jalin.jalinappbackend.module.gamification.leaderboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeaderboardDto {
    private Integer rank;
    private String jalinId;
    private Integer points;
}
