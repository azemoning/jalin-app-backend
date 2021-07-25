package com.jalin.jalinappbackend.module.gamification.checkin.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.checkin.model.CheckInDto;
import com.jalin.jalinappbackend.module.gamification.checkin.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.map.api}/v1")
public class CheckInController {
    @Autowired
    private CheckInService checkInService;

    @GetMapping("/check-in")
    public ResponseEntity<Object> getCheckInStatus() {
        CheckInDto checkInStatus = checkInService.getCheckInStatus();
        return new ResponseEntity<>(checkInStatus, HttpStatus.OK);
    }

    @PostMapping("/check-in")
    public ResponseEntity<Object> checkIn() {
        checkInService.checkIn();
        return new ResponseEntity<>(
                new SuccessResponse(true, "User successfully checked-in"),
                HttpStatus.CREATED);
    }

    @PostMapping("/check-in/reset")
    public ResponseEntity<Object> resetCheckIn() {
        checkInService.runCronJob();
        return new ResponseEntity<>(
                new SuccessResponse(true, "Cron job successfully executed"),
                HttpStatus.OK);
    }
}
