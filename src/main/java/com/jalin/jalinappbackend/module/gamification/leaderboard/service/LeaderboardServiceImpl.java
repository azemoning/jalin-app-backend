package com.jalin.jalinappbackend.module.gamification.leaderboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.authentication.service.UserService;
import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.LeaderboardRepository;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.ListPointRankDto;
import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardServiceImpl implements LeaderboardService{
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;
    @Autowired
    private ModelMapperUtility mapperUtility;

    @Override
    public Map<Object, Object> getUserPointOnLeaderboard() {

        User user = getSignedInUser();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Integer pointUser = point.getTotalPoints();

        Map<Object, Object> leaderboard = new HashMap<>();

//        leaderboard.put("current_rank", userRepository.findById(getSignedInUser().getUserId()) );
        leaderboard.put("total_point", pointUser);
        leaderboard.put("leaderboard", getListLeaderboard());

        return leaderboard;
    }

    @Override
    public List<ListPointRankDto> getListLeaderboard() {
        return leaderboardRepository.findUsersLeaderboard();
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
