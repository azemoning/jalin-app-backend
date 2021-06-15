package com.jalin.jalinappbackend.module.gamification.point.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.*;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointDetailRepository;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailRepository pointDetailRepository;

    @Autowired
    private PointSourceRepository pointSourceRepository;

    @Override
    public void initiateUserPoint(User user) {
        Point point = new Point();
        point.setUser(user);
        pointRepository.save(point);
    }

    @Override
    public void addUserPoint(PointSourceEnum sourceName, UUID sourceId, Integer pointAmount) {
        User user = getSignedInUser();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        point.setTotalPoints(point.getTotalPoints() + pointAmount);
        Point savedUserPoint = pointRepository.save(point);

        PointDetail pointDetail = initiateUserPointDetail(savedUserPoint, PointTypeEnum.ADD, pointAmount);
        initiateUserPointSource(pointDetail, sourceName, sourceId);
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private PointDetail initiateUserPointDetail(Point point, PointTypeEnum pointTypeEnum, Integer pointAmount) {
        PointDetail pointDetail = new PointDetail();
        pointDetail.setPoint(point);
        pointDetail.setPointTypeEnum(pointTypeEnum);
        pointDetail.setPointAmount(pointAmount);
        return pointDetailRepository.save(pointDetail);
    }

    private void initiateUserPointSource(PointDetail pointDetail, PointSourceEnum sourceName, UUID sourceId) {
        PointSource pointSource = new PointSource();
        if (sourceName == PointSourceEnum.CHECK_IN) {
//            CheckIn checkIn = checkInRepository.findById(sourceId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Check in not found"));

            pointSource.setPointDetail(pointDetail);
            pointSource.setSourceName(sourceName);
//            pointSource.setCheckIn(checkIn);
            pointSourceRepository.save(pointSource);
        }
    }
}
