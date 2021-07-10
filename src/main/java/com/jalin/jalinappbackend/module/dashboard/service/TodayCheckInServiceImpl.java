package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.model.TodayCheckInDto;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInLogRepository;
import com.jalin.jalinappbackend.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodayCheckInServiceImpl implements TodayCheckInService{
    @Autowired
    private UserUtility userUtility;
    @Autowired
    private CheckInLogRepository checkInLogRepository;
    @Autowired
    private ActiveCustomerService activeCustomerService;

    @Override
    public TodayCheckInDto getTodayCheckIn() {
        Integer totalUsers = userUtility.getAllUsers().size();
        Integer totalCheckIn = checkInLogRepository.findAll().size();
        return new TodayCheckInDto(totalUsers, totalCheckIn);
    }
}
