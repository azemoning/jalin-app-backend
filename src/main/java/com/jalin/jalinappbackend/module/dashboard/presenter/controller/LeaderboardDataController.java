package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.service.LeaderboardDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class LeaderboardDataController {
    @Autowired
    LeaderboardDataService leaderboardDataService;

    @GetMapping("leaderboardData")
    public ResponseEntity<Object> getLeaderboardData() {
        return new ResponseEntity<>(leaderboardDataService.getLeaderboardViewedByAdmin(), HttpStatus.OK);
    }
}
