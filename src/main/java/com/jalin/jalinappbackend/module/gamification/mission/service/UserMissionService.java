package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.module.gamification.mission.model.UserMissionDto;

import java.util.Set;

public interface UserMissionService {
    void assignUserMission();
    void checkUserMissionCompletion();
    Set<UserMissionDto> getUserMissions();
}
