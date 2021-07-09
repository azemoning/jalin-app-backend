package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionTakenServiceImpl implements MissionTakenService {

    @Autowired
    private UserMissionRepository userMissionRepository;

    @Override
    public int getTotalMissionsTaken() {
        List<UserMission> userMissionList = userMissionRepository.findAll();
        return userMissionList.size();
    }
}
