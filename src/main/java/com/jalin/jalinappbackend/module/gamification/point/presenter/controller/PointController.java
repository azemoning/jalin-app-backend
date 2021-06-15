package com.jalin.jalinappbackend.module.gamification.point.presenter.controller;

import com.jalin.jalinappbackend.module.gamification.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PointController {

    @Autowired
    private PointService pointService;

    @GetMapping("/points")
    public ResponseEntity<Object> getUserPoint() {
        return new ResponseEntity<>(pointService.getUserPoint(), HttpStatus.OK);
    }
}
