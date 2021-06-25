package com.jalin.jalinappbackend.module.gamification.mission.service;

import com.jalin.jalinappbackend.exception.ClaimMissionPointFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.banking.entity.Transaction;
import com.jalin.jalinappbackend.module.banking.repository.TransactionRepository;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMissionHistory;
import com.jalin.jalinappbackend.module.gamification.mission.model.UserMissionDto;
import com.jalin.jalinappbackend.module.gamification.mission.repository.MissionRepository;
import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionHistoryRepository;
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
import java.util.*;

@Service
public class UserMissionServiceImpl implements UserMissionService {

    @Autowired
    private UserMissionRepository userMissionRepository;

    @Autowired
    private UserMissionHistoryRepository userMissionHistoryRepository;

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
    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT+7.00")
    public void assignUserMission() {
        Random random = new Random();

        List<Mission> weeklyMissions = missionRepository.findMissionsByExpirationEquals("WEEKLY");
        List<Mission> biweeklyMissions = missionRepository.findMissionsByExpirationEquals("BIWEEKLY");
        List<Mission> monthlyMissions = missionRepository.findMissionsByExpirationEquals("MONTHLY");

        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (userMissionHistoryRepository.count() == 0) {
                // Weekly Mission
                Mission randomWeeklyMission = weeklyMissions.get(random.nextInt(weeklyMissions.size()));
                UserMission userMissionWeekly = new UserMission();
                initiateMission(randomWeeklyMission, user, userMissionWeekly);

                // Biweekly Mission
                Mission randomBiweeklyMission = biweeklyMissions.get(random.nextInt(biweeklyMissions.size()));
                UserMission userMissionBiweekly = new UserMission();
                initiateMission(randomBiweeklyMission, user, userMissionBiweekly);

                // Monthly Mission
                Mission randomMonthlyMission = monthlyMissions.get(random.nextInt(monthlyMissions.size()));
                UserMission userMissionMonthly = new UserMission();
                initiateMission(randomMonthlyMission, user, userMissionMonthly);

            } else if (userMissionHistoryRepository.count() >= 0) {
                List<UserMissionHistory> userMissionHistories = userMissionHistoryRepository
                        .findUserMissionHistoriesByUser(user);

                List<Mission> randomWeeklyMission = new ArrayList<>();
                List<Mission> randomBiweeklyMission = new ArrayList<>();
                List<Mission> randomMonthlyMission = new ArrayList<>();

                for (UserMissionHistory userMissionHistory : userMissionHistories) {

                    // Weekly Mission
                    for (Mission mission : weeklyMissions) {
                        if (!userMissionHistory.getMission().equals(mission)) {
                            randomWeeklyMission.add(mission);
                        } else {
                            if (userMissionHistory.getUserMission().getEndDate().compareTo(LocalDate.now()) < 0) {
                                break;
                            } else if (userMissionHistory.getUserMission().getEndDate().compareTo(LocalDate.now()) > 0){
                                UserMissionHistory oldUserMissionWeeklyHistory = userMissionHistoryRepository
                                        .findUserMissionHistoryByUserAndMission(user, mission);
                                userMissionHistoryRepository.delete(oldUserMissionWeeklyHistory);

                                UserMission userMissionWeekly = new UserMission();
                                Mission newUserWeeklyMission = randomWeeklyMission
                                        .get(random.nextInt(randomWeeklyMission.size()));

                                initiateMission(
                                        newUserWeeklyMission,
                                        user,
                                        userMissionWeekly
                                );

                                UserMissionHistory newUserMissionWeeklyHistory = new UserMissionHistory();
                                newUserMissionWeeklyHistory.setUser(user);
                                newUserMissionWeeklyHistory.setMission(newUserWeeklyMission);

                                userMissionHistoryRepository.save(newUserMissionWeeklyHistory);
                            }
                        }
                    }

                    // Biweekly Mission
                    for (Mission mission : biweeklyMissions) {
                        if (!userMissionHistory.getMission().equals(mission)) {
                            randomBiweeklyMission.add(mission);
                        } else {
                            if (userMissionHistory.getUserMission().getEndDate().compareTo(LocalDate.now()) < 0) {
                                break;
                            } else if (userMissionHistory.getUserMission().getEndDate().compareTo(LocalDate.now()) > 0){
                                UserMissionHistory oldUserMissionBiweeklyHistory = userMissionHistoryRepository
                                        .findUserMissionHistoryByUserAndMission(user, mission);
                                userMissionHistoryRepository.delete(oldUserMissionBiweeklyHistory);

                                UserMission userMissionBiweekly = new UserMission();
                                Mission newUserBiweeklyMission = randomBiweeklyMission
                                        .get(random.nextInt(randomBiweeklyMission.size()));

                                initiateMission(
                                        newUserBiweeklyMission,
                                        user,
                                        userMissionBiweekly
                                );

                                UserMissionHistory newUserMissionBiweeklyHistory = new UserMissionHistory();
                                newUserMissionBiweeklyHistory.setUser(user);
                                newUserMissionBiweeklyHistory.setMission(newUserBiweeklyMission);

                                userMissionHistoryRepository.save(newUserMissionBiweeklyHistory);
                            }
                        }
                    }

                    // Monthly Mission
                    for (Mission mission : monthlyMissions) {
                        if (!userMissionHistory.getMission().equals(mission)) {
                            randomMonthlyMission.add(mission);
                        } else {
                            if (userMissionHistory.getUserMission().getEndDate().compareTo(LocalDate.now()) < 0) {
                                break;
                            } else if (userMissionHistory.getUserMission().getEndDate().compareTo(LocalDate.now()) > 0){
                                UserMissionHistory oldUserMissionMonthlyHistory = userMissionHistoryRepository
                                        .findUserMissionHistoryByUserAndMission(user, mission);
                                userMissionHistoryRepository.delete(oldUserMissionMonthlyHistory);

                                UserMission userMissionMonthly = new UserMission();
                                Mission newUserMonthlyMission = randomMonthlyMission
                                        .get(random.nextInt(randomMonthlyMission.size()));

                                initiateMission(
                                        newUserMonthlyMission,
                                        user,
                                        userMissionMonthly
                                );

                                UserMissionHistory newUserMissionMonthlyHistory = new UserMissionHistory();
                                newUserMissionMonthlyHistory.setUser(user);
                                newUserMissionMonthlyHistory.setMission(newUserMonthlyMission);

                                userMissionHistoryRepository.save(newUserMissionMonthlyHistory);
                            }
                        }
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
        Mission mission = missionService.getMissionById(userMission.getMission().getId());
        if (!userMission.getStatus().equals("COMPLETED")) {
            userMission.setMissionProgress(userMission.getMissionProgress() + 1);

            if (userMission.getMissionProgress().equals(mission.getFrequency())) {
                userMission.setStatus("COMPLETED");
                userMission.setCompletionTime(LocalTime.now());
            }

            userMissionRepository.save(userMission);
        }
    }

    // Need to be refactored
    @Override
    public Set<UserMissionDto> getUserMissions() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User signedInUser = getSignedInUser();
        List<UserMission> userMissions = userMissionRepository.findUserMissionsByUser(signedInUser);
        Set<UserMissionDto> userMissionData = new HashSet<>();
        for (UserMission userMission: userMissions) {
            Mission mission = missionService.getMissionById(userMission.getMission().getId());
            if (userMission.getIsActive()) {
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

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void initiateMission(Mission mission, User user, UserMission userMission) {
        userMission.setUser(user);
        userMission.setMission(mission);
        userMission.setStartDate(LocalDate.now());

        switch (mission.getExpiration()) {
            case "WEEKLY":
            case "weekly":
                userMission.setEndDate(LocalDate.now().plusWeeks(1));
            case "BIWEEKLY":
            case "biweekly":
                userMission.setEndDate(LocalDate.now().plusWeeks(2));
            case "MONTHLY":
            case "monthly":
                userMission.setEndDate(LocalDate.now().plusMonths(1));
        }

        userMission.setMissionProgress(0);
        userMission.setStatus("INCOMPLETE");
        userMission.setIsClaimed(false);
        userMission.setIsActive(true);

        userMissionRepository.save(userMission);
    }
}