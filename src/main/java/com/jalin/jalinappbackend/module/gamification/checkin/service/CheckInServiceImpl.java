package com.jalin.jalinappbackend.module.gamification.checkin.service;

import com.jalin.jalinappbackend.exception.CheckInFailedException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckInCounter;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckInLog;
import com.jalin.jalinappbackend.module.gamification.checkin.model.CheckInDto;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInCounterRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInLogRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.PointSourceEnum;
import com.jalin.jalinappbackend.module.gamification.point.service.PointService;
import com.jalin.jalinappbackend.utility.UserUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CheckInServiceImpl implements CheckInService {
    private final static Integer MAX_COUNTER = 7;
    private final static Integer ONE_DAY_STREAK_POINTS = 5;
    private final static Integer TWO_DAYS_STREAK_POINTS = 10;
    private final static Integer THREE_DAYS_STREAK_POINTS = 15;
    private final static Integer FOUR_DAYS_STREAK_POINTS = 20;
    private final static Integer FIVE_DAYS_STREAK_POINTS = 25;
    private final static Integer SIX_DAYS_STREAK_POINTS = 30;
    private final static Integer SEVEN_DAYS_STREAK_POINTS = 50;
    @Autowired
    private UserUtility userUtility;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CheckInCounterRepository checkInCounterRepository;
    @Autowired
    private CheckInRepository checkInRepository;
    @Autowired
    private CheckInLogRepository checkInLogRepository;
    @Autowired
    private PointService pointService;

    @Override
    public void initiateCheckInCounter(User user) {
        CheckInCounter newCheckInCounter = new CheckInCounter();
        newCheckInCounter.setUser(user);
        checkInCounterRepository.save(newCheckInCounter);
    }

    @Override
    public CheckInDto getCheckInStatus() {
        User signedInUser = getSignedInUser();
        CheckInDto checkInDto = new CheckInDto();
        checkInDto.setCounter(getCurrentCounter(signedInUser));

        checkInRepository.findFirstByUserOrderByCreatedDateDesc(signedInUser)
                .ifPresentOrElse(
                        checkIn -> checkInDto.setLastCheckedIn(checkIn.getCreatedDate()),
                        () -> checkInDto.setLastCheckedIn(null));

        checkInLogRepository.findByUser(signedInUser)
                .ifPresentOrElse(
                        checkInLog -> checkInDto.setIsCheckedIn(true),
                        () -> checkInDto.setIsCheckedIn(false));

        return checkInDto;
    }

    @Override
    public void checkIn() {
        User signedInUser = getSignedInUser();
        validateCheckIn(signedInUser);
        increaseCounter(signedInUser);
        CheckIn checkInEntity = saveCheckIn(signedInUser);
        saveCheckInLog(signedInUser);
        increasePoint(checkInEntity.getCheckInId(), getCurrentCounter(signedInUser));
    }

    @Override
    public void runCronJob() {
        executeCheckInCronJob();
    }

    @Scheduled(cron = "0 0 17 * * ?", zone="GMT+7.00")
    private void executeCheckInCronJob() {
        resetCounterAllNotCheckedInUsers();
        resetCheckInLog();
        log.info("Check-in cron job successfully executed");
    }

    private Integer getCurrentCounter(User user) {
        CheckInCounter checkInCounter = checkInCounterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User's counter data not found"));
        return checkInCounter.getCounter();
    }

    private User getSignedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void increaseCounter(User user) {
        CheckInCounter checkInCounter = checkInCounterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User's counter data not found"));

        if (checkInCounter.getCounter() >= MAX_COUNTER) {
            resetCounter(user);
        }
        checkInCounter.setCounter(checkInCounter.getCounter() + 1);
        checkInCounterRepository.save(checkInCounter);
    }

    private void increasePoint(UUID checkInId, Integer currentCounter) {
        switch (currentCounter) {
            case 1:
                pointService.addUserPoint(PointSourceEnum.CHECK_IN, checkInId, ONE_DAY_STREAK_POINTS);
                break;
            case 2:
                pointService.addUserPoint(PointSourceEnum.CHECK_IN, checkInId, TWO_DAYS_STREAK_POINTS);
                break;
            case 3:
                pointService.addUserPoint(PointSourceEnum.CHECK_IN, checkInId, THREE_DAYS_STREAK_POINTS);
                break;
            case 4:
                pointService.addUserPoint(PointSourceEnum.CHECK_IN, checkInId, FOUR_DAYS_STREAK_POINTS);
                break;
            case 5:
                pointService.addUserPoint(PointSourceEnum.CHECK_IN, checkInId, FIVE_DAYS_STREAK_POINTS);
                break;
            case 6:
                pointService.addUserPoint(PointSourceEnum.CHECK_IN, checkInId, SIX_DAYS_STREAK_POINTS);
                break;
            case 7:
                pointService.addUserPoint(PointSourceEnum.CHECK_IN, checkInId, SEVEN_DAYS_STREAK_POINTS);
                break;
            default:
                throw new CheckInFailedException("Invalid counter value");
        }
    }

    private void resetCheckInLog() {
        checkInLogRepository.deleteAll();
    }

    private void resetCounter(User user) {
        CheckInCounter checkInCounter = checkInCounterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User's counter data not found"));
        checkInCounter.setCounter(0);
        checkInCounterRepository.save(checkInCounter);
    }

    private void resetCounterAllNotCheckedInUsers () {
        List<User> userList = userUtility.getAllUsers();
        for (User user : userList) {
            if (checkInLogRepository.findByUser(user).isEmpty()) {
                resetCounter(user);
            }
        }
    }

    private CheckIn saveCheckIn(User user) {
        CheckIn checkIn = new CheckIn();
        checkIn.setUser(user);
        return checkInRepository.save(checkIn);
    }

    private void saveCheckInLog(User user) {
        CheckInLog checkInLog = new CheckInLog();
        checkInLog.setUser(user);
        checkInLogRepository.save(checkInLog);
    }

    private void validateCheckIn(User user) {
        if (checkInLogRepository.findByUser(user).isPresent()) {
            throw new CheckInFailedException("User already check-in today");
        }
    }
}
