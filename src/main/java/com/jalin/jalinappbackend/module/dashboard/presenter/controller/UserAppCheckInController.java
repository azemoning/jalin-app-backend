package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.service.UserAppCheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class UserAppCheckInController {

    @Autowired
    private UserAppCheckInService userAppCheckInService;

    @GetMapping("userAppCheckin")
    public ResponseEntity<Object> getUserAppCheckIn(
            @RequestParam(name = "filter", required = false, defaultValue = "all") String filter,
            @RequestParam(name = "startDate", required = false, defaultValue = "all") String startDate,
            @RequestParam(name = "endDate", required = false, defaultValue = "all") String endDate
            ) {
        return new ResponseEntity<>(userAppCheckInService.getUserAppCheckIn(filter, startDate, endDate), HttpStatus.OK);
    }

}
