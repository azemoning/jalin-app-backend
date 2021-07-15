package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.LeaderboardDataDto;
import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.LeaderboardRepository;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeadrboardDataServiceImpl implements LeaderboardDataService{
    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserMissionService userMissionService;

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    @Override
    public Map<String, Object> getLeaderboardViewedByAdmin() {
        Map<String, Object> leaderboard = new HashMap<>();

        leaderboard.put("topThree", getLeadaerboardTop3());
        leaderboard.put("listLeaderboard", getLeaderboardAfterTop3());

        return leaderboard;
    }

    private List<LeaderboardDataDto> getLeadaerboardTop3(){
        return leaderboardRepository.getUserLeaderboardTop3InAdmin();
    }

    private List<LeaderboardDataDto> getLeaderboardAfterTop3(){
        return leaderboardRepository.getAlluserLeaderboardAfterTop3InAdmin();
    }
}
