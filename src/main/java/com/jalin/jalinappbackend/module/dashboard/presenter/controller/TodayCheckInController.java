package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.model.TodayCheckInDto;
import com.jalin.jalinappbackend.module.dashboard.service.TodayCheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class TodayCheckInController {
    @Autowired
    private TodayCheckInService todayCheckInService;

    @GetMapping("/check-in/today")
    public ResponseEntity<Object> getTodayCheckIn() {
        TodayCheckInDto todayCheckInDto = todayCheckInService.getTodayCheckIn();
        return new ResponseEntity<>(todayCheckInDto, HttpStatus.OK);
    }
}
