package com.jalin.jalinappbackend.module.gamification.point.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.point.entity.PointSourceEnum;
import com.jalin.jalinappbackend.module.gamification.point.model.PointDetailDto;
import com.jalin.jalinappbackend.module.gamification.point.model.PointDto;

import java.util.List;
import java.util.UUID;

public interface PointService {
    void initiateUserPoint(User user);
    void addUserPoint(PointSourceEnum sourceName, UUID sourceId, Integer pointAmount);
    PointDto getUserPoint();
    List<PointDetailDto> getUserPointDetails();

    // for QA
    void addUserPointQa(Integer amount);
    void resetUserPointQa();
}
