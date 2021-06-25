package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.exception.ClaimMissionPointFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.model.UserMissionDto;
import com.jalin.jalinappbackend.module.gamification.mission.repository.MissionRepository;
import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.PointSourceEnum;
import com.jalin.jalinappbackend.module.gamification.point.service.PointService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserMissionServiceImpl implements UserMissionService {

    @Autowired
    private UserMissionRepository userMissionRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionService missionService;

    @Autowired
    private PointService pointService;

    @Override
    @Scheduled(cron = "0 0 1 * * ?", zone = "GMT+7.00")
    public void assignUserMission() {
        Random random = new Random();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalDate today = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        LocalDate yesterday = today.minusDays(1);

        List<Mission> weeklyMissions = missionRepository.findMissionsByExpirationEquals("WEEKLY");
        List<Mission> biweeklyMissions = missionRepository.findMissionsByExpirationEquals("BIWEEKLY");
        List<Mission> monthlyMissions = missionRepository.findMissionsByExpirationEquals("MONTHLY");

        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<UserMission> userInactiveMissions = userMissionRepository
                    .findUserMissionsByUserAndIsActiveEquals(user, false);

            for (UserMission userInactiveMission : userInactiveMissions) {
                if (userInactiveMission.getEndDate().equals(yesterday)) {
                    switch (userInactiveMission.getMission().getExpiration()) {
                        case "WEEKLY":
                            List<Mission> weeklyMissionsWithExclusion = new ArrayList<>();
                            for (Mission weeklyMission : weeklyMissions) {
                                if (!weeklyMission.getId().equals(userInactiveMission.getMission().getId())) {
                                    weeklyMissionsWithExclusion.add(weeklyMission);
                                }
                            }

                            Mission getRandomWeeklyMission = weeklyMissionsWithExclusion
                                    .get(random.nextInt(weeklyMissionsWithExclusion.size()));

                            addUserMission(getRandomWeeklyMission, user);

                        case "BIWEEKLY":
                            List<Mission> biweeklyMissionsWithExclusion = new ArrayList<>();
                            for (Mission biweeklyMission : biweeklyMissions) {
                                if (!biweeklyMission.getId().equals(userInactiveMission.getMission().getId())) {
                                    biweeklyMissionsWithExclusion.add(biweeklyMission);
                                }
                            }

                            Mission getRandomBiweeklyMission = biweeklyMissionsWithExclusion
                                    .get(random.nextInt(biweeklyMissionsWithExclusion.size()));

                            addUserMission(getRandomBiweeklyMission, user);

                        case "MONTHLY":
                            List<Mission> monthlyMissionsWithExclusion = new ArrayList<>();
                            for (Mission monthlyMission : monthlyMissions) {
                                if (!monthlyMission.getId().equals(userInactiveMission.getMission().getId())) {
                                    monthlyMissionsWithExclusion.add(monthlyMission);
                                }
                            }

                            Mission getRandomMonthlyMission = monthlyMissionsWithExclusion
                                    .get(random.nextInt(monthlyMissionsWithExclusion.size()));

                            addUserMission(getRandomMonthlyMission, user);
                    }
                }
            }
        }
    }

    @Override
    public void checkUserMissionProgress() {
        Transaction transaction = transactionRepository
                .findTopByUserAndTransactionTypeEqualsOrderByCreatedDateDesc(
                        getSignedInUser(),
                        "C");
        Set<UserMissionDto> userMissionDtos = getUserMissions();

        for (UserMissionDto userMission : userMissionDtos) {
            if (userMission.getActivity().equals(transaction.getTransactionName())) {
                double transactionAmount = transaction.getAmount().doubleValue();
                double userMissionMinAmount = userMission.getMinimumAmount().doubleValue();
                UserMission updateUserMission = userMissionRepository.getById(userMission.getId());
                if (transactionAmount >= userMissionMinAmount) {
                    updateUserMissionProgress(updateUserMission);
                }
            }
        }

    }

    @Override
    public void updateUserMissionProgress(UserMission userMission) {
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalTime localTimeNow = LocalTime.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));

        Mission mission = missionService.getMissionById(userMission.getMission().getId());
        if (!userMission.getStatus().equals("COMPLETED")) {
            userMission.setMissionProgress(userMission.getMissionProgress() + 1);

            if (userMission.getMissionProgress().equals(mission.getFrequency())) {
                userMission.setStatus("COMPLETED");
                userMission.setCompletionTime(localTimeNow);
            }

            userMissionRepository.save(userMission);
        }
    }

    @Override
    public Set<UserMissionDto> getUserMissions() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User signedInUser = getSignedInUser();
        List<UserMission> userMissions = userMissionRepository
                .findUserMissionsByUserAndIsActiveEquals(signedInUser, true);
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

    @Override
    public void claimCompletedMissionPoint(UUID userMissionId) {
        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new ResourceNotFoundException("User mission not found"));

        Mission mission = missionService.getMissionById(userMission.getMission().getId());

        if (userMission.getStatus().equals("COMPLETED")) {
            userMission.setIsClaimed(true);
            userMission.setIsActive(false);
            userMissionRepository.save(userMission);
            pointService.addUserPoint(PointSourceEnum.MISSION, mission.getId(), mission.getPoint());
        } else if (userMission.getStatus().equals("INCOMPLETE")){
            throw new ClaimMissionPointFailedException("Mission not completed yet");
        } else if (userMission.getIsClaimed().equals(true)) {
            throw new ClaimMissionPointFailedException("Mission point already claimed");
        }
    }

    @Override
    public void initiateUserMission() {
        Random random = new Random();
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);

        List<User> users = userRepository.findAll();

        List<Mission> weeklyMissions = missionRepository.findMissionsByExpirationEquals("WEEKLY");
        List<Mission> biweeklyMissions = missionRepository.findMissionsByExpirationEquals("BIWEEKLY");
        List<Mission> monthlyMissions = missionRepository.findMissionsByExpirationEquals("MONTHLY");

        Mission randomWeeklyMission = weeklyMissions.get(random.nextInt(weeklyMissions.size()));
        Mission randomBiweeklyMission = biweeklyMissions.get(random.nextInt(biweeklyMissions.size()));
        Mission randomMonthlyMission = monthlyMissions.get(random.nextInt(monthlyMissions.size()));

        for (User user : users) {
            addUserMission(randomWeeklyMission, user);
            addUserMission(randomBiweeklyMission, user);
            addUserMission(randomMonthlyMission, user);
        }
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void addUserMission(Mission mission, User user) {

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalDate localDateNow = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        UserMission userMission = new UserMission();

        userMission.setUser(user);
        userMission.setMission(mission);
        userMission.setStartDate(localDateNow);

        switch (mission.getExpiration()) {
            case "WEEKLY":
                userMission.setEndDate(localDateNow.plusWeeks(1));
                break;
            case "BIWEEKLY":
                userMission.setEndDate(localDateNow.plusWeeks(2));
                break;
            case "MONTHLY":
                userMission.setEndDate(localDateNow.plusMonths(1));
                break;
        }

        userMission.setMissionProgress(0);
        userMission.setStatus("INCOMPLETE");
        userMission.setIsClaimed(false);
        userMission.setIsActive(true);

        userMissionRepository.save(userMission);
    }
}