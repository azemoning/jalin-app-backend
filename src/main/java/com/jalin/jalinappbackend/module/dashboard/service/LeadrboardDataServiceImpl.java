package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.LeaderboardDataDto;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.LeaderboardAdminDto;
import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.LeaderboardRepository;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<LeaderboardAdminDto> listPointRankDtos = leaderboardRepository.getUserLeaderboardTop3InAdmin();
        List<LeaderboardDataDto> listDtos = new ArrayList<>();
        for (LeaderboardAdminDto data: listPointRankDtos){
            LeaderboardDataDto leaderboardDataDto = modelMapperUtility.
                    initialize().map(data, LeaderboardDataDto.class);
//            Object ini = leaderboardDataDto.setMissionSolved(userMissionService.getTotalUserCompletedMissions(data.getJalinId()));
            listDtos.add(leaderboardDataDto);
        }
        return listDtos;
    }

    private List<LeaderboardDataDto> getLeaderboardAfterTop3(){
        List<LeaderboardAdminDto> listPointRankDtos = leaderboardRepository.getAlluserLeaderboardAfterTop3();
        List<LeaderboardDataDto> listDtos = new ArrayList<>();
        for (LeaderboardAdminDto data: listPointRankDtos){
            LeaderboardDataDto leaderboardDataDto = modelMapperUtility.
                    initialize().map(data, LeaderboardDataDto.class);
//            leaderboardDataDto.setMissionSolved(userMissionService.getTotalUserCompletedMissions(data.getJalinId()));

            listDtos.add(leaderboardDataDto);
        }
        return listDtos;
    }
}
