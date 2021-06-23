package com.jalin.jalinappbackend.module.gamification.mission.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class UserMissionController {

    @Autowired
    private UserMissionService userMissionService;

    @PostMapping("mission")
    public ResponseEntity<Object> assignMission() {
        userMissionService.assignUserMission();
        return new ResponseEntity<>(new SuccessResponse(true, "Mission assigned to user"),
                HttpStatus.CREATED);
    }

    @GetMapping("mission")
    public ResponseEntity<Object> getUserMissions() {
        return new ResponseEntity<>(userMissionService.getUserMissions(), HttpStatus.OK);
    }

}
