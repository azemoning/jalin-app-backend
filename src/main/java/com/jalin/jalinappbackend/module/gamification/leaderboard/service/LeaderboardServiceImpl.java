package com.jalin.jalinappbackend.module.gamification.leaderboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.LeaderboardRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class LeaderboardServiceImpl implements LeaderboardService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Override
    public Map<Object, Object> getUserPointOnLeaderboard() {

        User user = getSignedInUser();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Integer pointUser = point.getTotalPoints();

        Map<Object, Object> leaderboard = new HashMap<>();

        leaderboard.put("current_rank", "");
        leaderboard.put("total_point", pointUser);
        leaderboard.put("leaderboard", getListLeaderboard());

        return leaderboard;
    }

    public ArrayList<Object> getListLeaderboard(){
        return leaderboardRepository.findUsersLeaderboard();
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
