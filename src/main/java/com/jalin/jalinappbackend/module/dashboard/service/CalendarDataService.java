package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.CalendarDataDto;

import java.util.List;

public interface CalendarDataService {
    List<CalendarDataDto> getCalendarData();
}
