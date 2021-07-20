package com.jalin.jalinappbackend.module.gamification.mission.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class UserMissionController {

    @Autowired
    private UserMissionService userMissionService;

    @GetMapping("mission")
    public ResponseEntity<Object> getUserMissions(
            @RequestParam(name = "expiration", defaultValue = "all") String expiration
    ) {
        return new ResponseEntity<>(userMissionService.getUserMissions(expiration), HttpStatus.OK);
    }

    @PostMapping(path = "mission/claim", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> claimUserCompletedMissionPoint(@RequestBody UUID userMissionId) {
        userMissionService.claimCompletedMissionPoint(userMissionId);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Mission point claimed successfully"),
                HttpStatus.OK);
    }

    @PostMapping("mission/forceAssign")
    public ResponseEntity<Object> forceAssignUserMission(
            @RequestParam(name = "expiration") String expiration
    ) {
        userMissionService.forceAssignUserMission(expiration);
        return new ResponseEntity<>(new SuccessResponse(
                true,
                "User mission reasigned"
        ), HttpStatus.OK);
    }

    @PostMapping("mission/forceComplete")
    public ResponseEntity<Object> forceCompleteUserMission(
            @RequestParam(name = "expiration") String expiration
    ) {
        userMissionService.forceCompleteUserMission(expiration);
        return new ResponseEntity<>(new SuccessResponse(
                true,
                "Successfully forcing user mission to complete"),
                HttpStatus.OK
        );
    }

}
