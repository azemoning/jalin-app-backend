package com.jalin.jalinappbackend.module.gamification.point.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.point.entity.PointSourceEnum;

import java.util.UUID;

public interface PointService {
    void initiateUserPoint(User user);
    void addUserPoint(PointSourceEnum sourceName, UUID sourceId, Integer pointAmount);
    Integer getUserPoint();
}
