package com.jalin.jalinappbackend.module.gamification.mission.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/admin/v1")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping("mission")
    public ResponseEntity<Object> getAllMissions() {
        return new ResponseEntity<>(missionService.getAllMissions(), HttpStatus.OK);
    }

    @PostMapping("mission")
    public ResponseEntity<Object> addMission(@RequestBody Mission mission) {
        missionService.addMission(mission);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Mission added successfully"),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("mission/{missionId}")
    public ResponseEntity<Object> deleteMission(@PathVariable UUID missionId) {
        missionService.deleteMission(missionId);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Mission deleted successfully"),
                HttpStatus.OK
        );
    }

}
