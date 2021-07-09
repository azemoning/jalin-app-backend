package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.dashboard.service.CalendarDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/v1")
public class CalendarDataController {

    @Autowired
    private CalendarDataService calendarDataService;

    @GetMapping("calendarData")
    public ResponseEntity<Object> getCalendarData() {
        return new ResponseEntity<>(calendarDataService.getCalendarData(), HttpStatus.OK);
    }

    @PostMapping("updateCalendarData")
    public ResponseEntity<Object> updateCalendarData() {
        calendarDataService.updateCalendarData();
        return new ResponseEntity<>(
                new SuccessResponse(true, "Calendar data updated"), HttpStatus.OK
        );
    }
}
