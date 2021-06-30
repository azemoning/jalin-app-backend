package com.jalin.jalinappbackend.module.gamification.checkin.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.checkin.model.CheckInDto;

public interface CheckInService {
    void initiateCheckInCounter(User user);
    CheckInDto getCheckInStatus();
    void checkIn();
    void runCronJob();
}
