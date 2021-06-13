package com.jalin.jalinappbackend.module.gamification.point.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.Point;
import com.jalin.jalinappbackend.module.gamification.point.entity.PointDetail;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointDetailRepository;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailRepository pointDetailRepository;

    @Override
    public void addUserPoint(UUID userId, Integer amount, String pointSource) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Point point = pointRepository.findByUserId(user)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        PointDetail pointDetail = new PointDetail(point, amount, pointSource);
        point.setTotal(point.getTotal() + pointDetail.getAmount());
        pointDetailRepository.save(pointDetail);
        pointRepository.save(point);
    }

    @Override
    public Integer getUserPoint(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Point point = pointRepository.findByUserId(user)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return point.getTotal();
    }
}
