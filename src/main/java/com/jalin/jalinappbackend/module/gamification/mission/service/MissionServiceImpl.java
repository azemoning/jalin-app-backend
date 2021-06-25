package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.exception.AddMissionFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MissionServiceImpl implements MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Override
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    @Override
    public Mission getMissionById(UUID missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));
    }

    @Override
    public void addMission(Mission mission) {
        List<Mission> allMissions = missionRepository.findAll();

        // replace lower case expiration with upper case
        switch (mission.getExpiration()) {
            case "weekly":
                mission.setExpiration("WEEKLY");
            case "biweekly":
                mission.setExpiration("BIWEEKLY");
            case "monthly":
                mission.setExpiration("MONTHLY");
        }

        if (!allMissions.isEmpty()) {
            for (Mission data : allMissions) {
                if (mission.getActivity().equals(data.getActivity()) && mission.getFrequency().equals(data.getFrequency())
                && mission.getMinAmount().equals(data.getMinAmount()) && mission.getExpiration().equals(data.getExpiration())
                && mission.getPoint().equals(data.getPoint())) {
                    throw new AddMissionFailedException("Mission with same detail are already exists");
                }
            }
        }
        missionRepository.save(mission);
    }

    @Override
    public void deleteMission(UUID missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));
        missionRepository.delete(mission);
    }
}
