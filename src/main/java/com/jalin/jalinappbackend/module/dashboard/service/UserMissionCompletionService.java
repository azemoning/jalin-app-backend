package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.UserMissionCompletionDto;

import java.util.List;

public interface UserMissionCompletionService {
    List<UserMissionCompletionDto> getUserMissionCompletion(String startDate, String endDate);
}
