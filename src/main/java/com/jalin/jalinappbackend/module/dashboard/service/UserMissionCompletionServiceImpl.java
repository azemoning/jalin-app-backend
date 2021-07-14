package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.dashboard.model.UserMissionCompletionDto;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserMissionCompletionServiceImpl implements UserMissionCompletionService {

    @Autowired
    private UserMissionRepository userMissionRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public List<UserMissionCompletionDto> getUserMissionCompletion() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<UserMission> userMissions = userMissionRepository.findUserMissionsByStatusEquals("COMPLETED");
        List<UserMissionCompletionDto> userMissionCompletionDtos = new ArrayList<>();

        for (UserMission userMission : userMissions) {
            UserMissionCompletionDto userMissionCompletionDto = modelMapper
                    .map(userMission, UserMissionCompletionDto.class);

            UserDetails userDetails = userDetailsRepository.findByUser(userMission.getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

            String hour, minute;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            if (userMission.getCompletionTime().getHour() < 10) {
                hour = "0" + userMission.getCompletionTime().getHour();
            } else {
                hour = Integer.toString(userMission.getCompletionTime().getHour());
            }

            if (userMission.getCompletionTime().getMinute() < 10) {
                minute = "0" + userMission.getCompletionTime().getMinute();
            } else {
                minute = Integer.toString(userMission.getCompletionTime().getMinute());
            }

            userMissionCompletionDto.setCid(userDetails.getJalinId());
            userMissionCompletionDto.setMissionTransaction(userMission.getMission().getActivity());
            userMissionCompletionDto.setFullName(userDetails.getFullName());
            userMissionCompletionDto.setPointsUnlocked(userMission.getMission().getPoint());
            userMissionCompletionDto.setStatus(userMission.getStatus());
            userMissionCompletionDto.setDate(userMission.getCompletionDate().format(dtf));
            userMissionCompletionDto.setTimeMark(hour + "." + minute);

            userMissionCompletionDtos.add(userMissionCompletionDto);
        }

        return userMissionCompletionDtos;
    }
}
