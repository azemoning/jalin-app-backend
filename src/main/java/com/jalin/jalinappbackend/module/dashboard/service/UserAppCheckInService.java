package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.UserAppCheckInDto;

import java.util.List;

public interface UserAppCheckInService {
    List<UserAppCheckInDto> getUserAppCheckIn();
}
