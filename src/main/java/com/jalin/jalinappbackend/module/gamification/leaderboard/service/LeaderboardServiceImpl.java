package com.jalin.jalinappbackend.module.gamification.leaderboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.authentication.service.UserService;
import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.LeaderboardRepository;
import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.model.PointDto;
import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        leaderboard.put("current_rank", "point");
        leaderboard.put("total_point", pointUser);
        leaderboard.put("leaderboard", getListLeaderboard());

        return leaderboard;
    }

    private List<PointDto> getListLeaderboard() {
        List<Point> pointList = pointRepository.findAll();
        List<PointDto> pointDTOS = new ArrayList<>();
        for (Point det : pointList){
            PointDto pointDto = mapperUtility.initialize().map(det, PointDto.class);
            pointDto.setFullName(userService.getUserDetails().getFullName());
            pointDTOS.add(pointDto);
        }

        return pointDTOS;
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
