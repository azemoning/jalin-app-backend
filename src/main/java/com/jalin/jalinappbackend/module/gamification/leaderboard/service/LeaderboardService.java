package com.jalin.jalinappbackend.module.gamification.leaderboard.service;

import com.jalin.jalinappbackend.module.gamification.leaderboard.model.LeaderboardDto;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.ListPointRankDto;

import java.util.List;
import java.util.Map;

public interface LeaderboardService {
    Map<Object, Object> getUserPointOnLeaderboard();
    List<ListPointRankDto> findUserRank(String name);
    List<LeaderboardDto> getLeaderboardData();
}
