package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MissionCompleteServiceImpl implements MissionCompleteService {

    @Autowired
    private UserMissionRepository userMissionRepository;


    @Override
    public int getTotalMissionComplete() {
        return userMissionRepository.findUserMissionsByStatusEqualsOrderByModifiedDateDesc("COMPLETED").size();
    }
}
