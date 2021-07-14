package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.service.UserMissionCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/v1")
public class UserMissionCompletionController {
    @Autowired
    private UserMissionCompletionService userMissionCompletionService;

    @GetMapping("userMissionCompletion")
    public ResponseEntity<Object> getUserMissionCompletion() {
        return new ResponseEntity<>(userMissionCompletionService.getUserMissionCompletion(), HttpStatus.OK);
    }
}
