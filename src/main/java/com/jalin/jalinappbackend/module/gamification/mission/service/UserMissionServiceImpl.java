package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.model.UserMissionDto;
import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserMissionServiceImpl implements UserMissionService {

    @Autowired
    private UserMissionRepository userMissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionService missionService;

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    @Override
    public void assignUserMission() {

        // For testing purpose
        // If we already have found the proper way to assign random missions to user
        // then this method will be replaced

        List<Mission> missions = missionService.getAllMissions();

        for (Mission mission : missions) {
            UserMission userMission = new UserMission();
            userMission.setMission(mission);
            userMission.setUser(getSignedInUser());
            userMission.setStartDate(LocalDate.now());

            switch (mission.getExpiration()) {
                case "weekly":
                    userMission.setEndDate(LocalDate.now().plusWeeks(1));
                case "biweekly":
                    userMission.setEndDate(LocalDate.now().plusWeeks(2));
                case "monthly":
                    userMission.setEndDate(LocalDate.now().plusMonths(1));
            }

            userMission.setMissionProgress(0);
            userMission.setStatus("incomplete");

            if (userMission.getStatus().equals("complete")) {
                userMission.setCompletionTime(LocalTime.now());
            }

            userMissionRepository.save(userMission);
        }
    }

    @Override
    public void checkUserMissionCompletion() {

    }

    // Need to be refactored
    @Override
    public Set<UserMissionDto> getUserMissions() {

        // Not using modelMapperUtility for now
        // See https://stackoverflow.com/questions/49831753/modelmapper-matches-multiple-source-property-hierarchies
        // Actually the matching strategy can be set on ModelMapperUtilityClass
        // But I'm afraid it will affects the data mapped to all of the entity dtos
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User signedInUser = getSignedInUser();
        List<UserMission> userMissions = userMissionRepository.findUserMissionsByUser(signedInUser);
        Set<UserMissionDto> userMissionData = new HashSet<>();
        for (UserMission userMission: userMissions) {
            Mission mission = missionService.getMissionById(userMission.getMission().getId());
            UserMissionDto userMissionDto = modelMapper.map(userMission, UserMissionDto.class);

            userMissionDto.setActivity(mission.getActivity());
            userMissionDto.setMissionDescription(mission.getMissionDescription());
            userMissionDto.setTncDescription(mission.getTncDescription());
            userMissionDto.setFrequency(mission.getFrequency());
            userMissionDto.setMinimumAmount(mission.getMinAmount());
            userMissionDto.setExpiration(mission.getExpiration());
            userMissionDto.setPoint(mission.getPoint());

            userMissionData.add(userMissionDto);
        }
        return userMissionData;
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
