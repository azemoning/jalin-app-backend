package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.exception.AddMissionFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.repository.MissionRepository;
import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class MissionServiceImpl implements MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserMissionRepository userMissionRepository;

    @Autowired
    private UserMissionService userMissionService;

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

        for (Mission data : allMissions) {
            if (mission.getActivity().equals(data.getActivity())) {
                if (mission.getFrequency().equals(data.getFrequency())) {
                    if (mission.getMinAmount().compareTo(data.getMinAmount()) == 0) {
                        if (mission.getExpiration().equals(data.getExpiration())) {
                            if (mission.getPoint().equals(data.getPoint())) {
                                throw new AddMissionFailedException("Mission with same detail are already exists");
                            }
                        }
                    }
                }
            }
        }

        missionRepository.save(mission);
    }

    @Override
    public void updateMission(UUID missionId, Mission mission) {
        Mission findMission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));

        findMission.setActivity(mission.getActivity());
        findMission.setMissionDescription(mission.getMissionDescription());
        findMission.setTncDescription(mission.getTncDescription());
        findMission.setFrequency(mission.getFrequency());
        findMission.setMinAmount(mission.getMinAmount());
        findMission.setExpiration(mission.getExpiration());
        findMission.setPoint(mission.getPoint());

        missionRepository.save(findMission);
    }

    @Override
    public void deleteMission(UUID missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));
        List<UserMission> userMissions = userMissionRepository.findUserMissionsByMission(mission);

        if (!userMissions.isEmpty()) {
            for (UserMission userMission : userMissions) {
                userMissionRepository.delete(userMission);
            }
        }
        missionRepository.delete(mission);
    }
}
