package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping("mission")
    public ResponseEntity<Object> getAllMissions() {
        return new ResponseEntity<>(missionService.getAllMissions(), HttpStatus.OK);
    }

    @GetMapping("mission/{missionId}")
    public ResponseEntity<Object> getMissionById(@PathVariable UUID missionId) {
        return new ResponseEntity<>(missionService.getMissionById(missionId), HttpStatus.OK);
    }

    @PostMapping("mission")
    public ResponseEntity<Object> addMission(@RequestBody Mission mission) {
        missionService.addMission(mission);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Mission added successfully"),
                HttpStatus.CREATED
        );
    }

    @PutMapping("mission/{missionId}")
    public ResponseEntity<Object> updateMission(@PathVariable UUID missionId, @RequestBody Mission mission) {
        missionService.updateMission(missionId, mission);
        return new ResponseEntity<>(
                new SuccessResponse(true, "Mission updated successfully"),
                HttpStatus.OK
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
