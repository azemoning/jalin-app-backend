package com.jalin.jalinappbackend.module.gamification.point.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.point.presenter.model.AddUserPointRequest;
import com.jalin.jalinappbackend.module.gamification.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PointController {

    @Autowired
    private PointService pointService;

    @GetMapping("/point/{userId}")
    public ResponseEntity<Object> getUserPoint(@PathVariable UUID userId) {
        return new ResponseEntity<>(
                new SuccessDetailsResponse(
                        true,
                        "OK",
                        pointService.getUserPoint(userId)
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/point/{userId}")
    public ResponseEntity<Object> addUserPoint(
            @PathVariable UUID userId,
            @RequestBody AddUserPointRequest requestBody
            ) {
        pointService.addUserPoint(userId, requestBody.getAmount(), requestBody.getPointSource());
        return new ResponseEntity<>(
                new SuccessResponse(true, "Point successfully added"),
                HttpStatus.CREATED
        );
    }

}
