package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.service.UserMissionCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class UserMissionCompletionController {
    @Autowired
    private UserMissionCompletionService userMissionCompletionService;

    @GetMapping("userMissionCompletion")
    public ResponseEntity<Object> getUserMissionCompletion(
            @RequestParam(name = "startDate", defaultValue = "all") String startDate,
            @RequestParam(name = "endDate", defaultValue = "all") String endDate
    ) {
        return new ResponseEntity<>(userMissionCompletionService.getUserMissionCompletion(startDate, endDate), HttpStatus.OK);
    }
}
