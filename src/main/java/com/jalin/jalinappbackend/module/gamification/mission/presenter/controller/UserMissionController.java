package com.jalin.jalinappbackend.module.gamification.mission.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class UserMissionController {

    @Autowired
    private UserMissionService userMissionService;

    @PostMapping("mission/assign")
    public ResponseEntity<Object> assignNewMission() {
        userMissionService.assignUserMission();
        return new ResponseEntity<>(new SuccessResponse(true, "Mission reassigned to user"),
                HttpStatus.OK);
    }

    @GetMapping("mission")
    public ResponseEntity<Object> getUserMissions() {
        return new ResponseEntity<>(userMissionService.getUserMissions(), HttpStatus.OK);
    }

    @PostMapping("mission/claim/{userMissionId}")
    public ResponseEntity<Object> claimUserompletedMissionPoint(@PathVariable UUID userMissionId) {
        userMissionService.claimCompletedMissionPoint(userMissionId);
        return new ResponseEntity<>(new SuccessResponse(true, "Mission point claimed successfully"),
                HttpStatus.OK);
    }

}
