package com.jalin.jalinappbackend.module.gamification.leaderboard.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PointDto {
    private UUID userId;
    private String fullName;
    private Integer totalPoints;
    private Integer rank;
}
