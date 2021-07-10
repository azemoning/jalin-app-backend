package com.jalin.jalinappbackend.module.gamification.leaderboard.presenter.controller;

import com.jalin.jalinappbackend.module.gamification.leaderboard.repository.LeaderboardRepository;
import com.jalin.jalinappbackend.module.gamification.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/leaderboard")
    public ResponseEntity<Object> getLeaderboard(){
        return new ResponseEntity<>(leaderboardService.getUserPointOnLeaderboard(), HttpStatus.OK);
    }

    @GetMapping("/leaderboard/search")
    public ResponseEntity<Object> searchUserRank(@RequestParam (value = "name", required = false) String name){
        return new ResponseEntity<>(leaderboardService.findUserRank(name), HttpStatus.OK);
    }
}
