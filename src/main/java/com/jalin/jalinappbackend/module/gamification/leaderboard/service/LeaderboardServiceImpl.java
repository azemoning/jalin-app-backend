package com.jalin.jalinappbackend.module.gamification.leaderboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.authentication.service.UserService;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.LeaderboardDto;
import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.LeaderboardRepository;
import com.jalin.jalinappbackend.module.gamification.leaderboard.model.ListPointRankDto;
import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LeaderboardServiceImpl implements LeaderboardService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
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

        leaderboard.put("currentUserPointAndRank", getUserRankAndPoint());
        leaderboard.put("topThree", getTopThree());
        leaderboard.put("listLeaderboard", getAfterTop3());

        return leaderboard;
    }

    @Override
    public List<ListPointRankDto> findUserRank(String name){
        return  leaderboardRepository.findUserInLeadrrboard(name);
    }

    @Override
    public List<LeaderboardDto> getLeaderboardData() {
        List<LeaderboardDto> leaderboardDtos = new ArrayList<>();

        List<Point> points = leaderboardRepository.findTop10ByOrderByTotalPointsDesc();
        List<Point> allPoints = leaderboardRepository.findAllByOrderByTotalPointsDesc();
        Point userPoint = leaderboardRepository.findPointByUser(getSignedInUser());

        for (Point point : points) {
            LeaderboardDto leaderboardDto = mapperUtility.initialize().map(point, LeaderboardDto.class);
            UserDetails userDetails = userDetailsRepository.findByUser(point.getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

            leaderboardDto.setRank(points.indexOf(point) + 1);
            leaderboardDto.setJalinId(userDetails.getJalinId());
            leaderboardDto.setPoints(point.getTotalPoints());

            leaderboardDtos.add(leaderboardDto);
        }

        for (Point point : allPoints) {
            if (point.getUser().equals(userPoint.getUser())) {
                LeaderboardDto leaderboardDto = mapperUtility.initialize().map(point, LeaderboardDto.class);
                UserDetails userDetails = userDetailsRepository.findByUser(point.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                leaderboardDto.setRank(allPoints.indexOf(point) + 1);
                leaderboardDto.setJalinId(userDetails.getJalinId());
                leaderboardDto.setPoints(point.getTotalPoints());

                leaderboardDtos.add(leaderboardDto);
            }
        }
        return leaderboardDtos;
    }

    private Optional<ListPointRankDto> getUserRankAndPoint(){
        String user = getSignedInUser().getEmail();
        return leaderboardRepository.getUserRankAndPoint(user);
    }

    private List<ListPointRankDto> getTopThree(){
        return leaderboardRepository.getUserLeaderBoardTop3();
    }

    private List<ListPointRankDto> getAfterTop3() {
        return leaderboardRepository.getUsersLeaderboard();
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
