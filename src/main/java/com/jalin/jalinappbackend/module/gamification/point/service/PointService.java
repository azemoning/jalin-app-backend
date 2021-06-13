package com.jalin.jalinappbackend.module.gamification.point.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;

import java.util.UUID;

public interface PointService {
    void addUserPoint(UUID userId, Integer amount, String pointSource);
    Integer getUserPoint(UUID userId);
}
