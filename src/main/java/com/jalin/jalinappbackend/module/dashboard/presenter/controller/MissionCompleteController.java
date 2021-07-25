package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.service.MissionCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class MissionCompleteController {

    @Autowired
    private MissionCompleteService missionCompleteService;

    @GetMapping("missionComplete")
    public ResponseEntity<Object> getTotalMissionComplete() {
        return new ResponseEntity<>(missionCompleteService.getTotalMissionComplete(), HttpStatus.OK);
    }
}
