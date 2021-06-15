package com.jalin.jalinappbackend.module.gamification.checkin.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;

public interface CheckInService {
    void initiateCheckInCounter(User user);
    void checkIn();
}
