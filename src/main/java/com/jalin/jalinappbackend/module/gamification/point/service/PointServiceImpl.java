package com.jalin.jalinappbackend.module.gamification.point.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInRepository;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.repository.MissionRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.*;
import com.jalin.jalinappbackend.module.gamification.point.model.PointDetailDto;
import com.jalin.jalinappbackend.module.gamification.point.model.PointDto;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointDetailRepository;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointRepository;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointSourceRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailRepository pointDetailRepository;

    @Autowired
    private PointSourceRepository pointSourceRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private MissionRepository missionRepository;

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

    @Override
    public PointDto getUserPoint() {
        User user = getSignedInUser();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User's point not found"));
        return new PointDto(point.getTotalPoints());
    }

    @Override
    public List<PointDetailDto> getUserPointDetails() {
        User user = getSignedInUser();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User's point not found"));
        List<PointDetail> pointDetailList = pointDetailRepository.findByPoint(point);

        List<PointDetailDto> pointDetailDtoList = new ArrayList<>();
        for (PointDetail pointDetail : pointDetailList) {
            PointSource pointSource = pointSourceRepository.findById(pointDetail.getPointDetailId())
                    .orElseThrow(() -> new ResourceNotFoundException("Point source not found"));
            PointDetailDto pointDetailDto = modelMapperUtility.initialize()
                    .map(pointDetail, PointDetailDto.class);
            pointDetailDto.setSourceName(pointSource.getSourceName().name());
            pointDetailDtoList.add(pointDetailDto);
        }

        return pointDetailDtoList;
    }

    @Override
    public void addUserPointQa(Integer amount) {
        User user = getSignedInUser();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User point not found"));

        point.setTotalPoints(amount);
        pointRepository.save(point);
    }

    @Override
    public void resetUserPointQa() {
        User user = getSignedInUser();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User point not found"));

        point.setTotalPoints(0);
        pointRepository.save(point);
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
        pointDetail.setPointType(pointTypeEnum);
        pointDetail.setPointAmount(pointAmount);
        return pointDetailRepository.save(pointDetail);
    }

    private void initiateUserPointSource(PointDetail pointDetail, PointSourceEnum sourceName, UUID sourceId) {
        PointSource pointSource = new PointSource();
        if (sourceName == PointSourceEnum.CHECK_IN) {
            CheckIn checkIn = checkInRepository.findById(sourceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Check in not found"));

            pointSource.setPointDetail(pointDetail);
            pointSource.setSourceName(sourceName);
            pointSource.setCheckIn(checkIn);
            pointSourceRepository.save(pointSource);
        } else if (sourceName == PointSourceEnum.MISSION) {
            Mission mission = missionRepository.findById(sourceId).
                    orElseThrow(() -> new ResourceNotFoundException("Mission id not found"));

            pointSource.setPointDetail(pointDetail);
            pointSource.setSourceName(sourceName);
            pointSource.setMission(mission);
            pointSourceRepository.save(pointSource);
        }
    }
}
