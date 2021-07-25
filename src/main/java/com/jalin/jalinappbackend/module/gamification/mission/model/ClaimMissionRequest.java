package com.jalin.jalinappbackend.module.gamification.mission.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimMissionRequest {
    private UUID userMissionId;
}
