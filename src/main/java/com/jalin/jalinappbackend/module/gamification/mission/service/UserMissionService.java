package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.model.UserMissionDto;

import java.util.Set;
import java.util.UUID;

public interface UserMissionService {
    void assignUserMission();
    void checkUserMissionProgress();
    void updateUserMissionProgress(UserMission userMission);
    Set<UserMissionDto> getUserMissions();
    void claimCompletedMissionPoint(UUID userMissionId);
}