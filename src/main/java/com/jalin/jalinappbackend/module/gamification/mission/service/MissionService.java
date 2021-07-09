package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;

import java.util.List;
import java.util.UUID;

public interface MissionService {
    List<Mission> getAllMissions();
    Mission getMissionById(UUID missionId);
    void addMission(Mission mission);
    void updateMission(UUID missionId, Mission mission);
    void deleteMission(UUID missionId);
}
