package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.dashboard.model.UserAppCheckInDto;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckInCounter;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckInLog;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInCounterRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInLogRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAppCheckInServiceImpl implements UserAppCheckInService {

    @Autowired
    private CheckInLogRepository checkInLogRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private CheckInCounterRepository checkInCounterRepository;

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    private final static Integer ONE_DAY_STREAK_POINTS = 5;
    private final static Integer TWO_DAYS_STREAK_POINTS = 10;
    private final static Integer THREE_DAYS_STREAK_POINTS = 15;
    private final static Integer FOUR_DAYS_STREAK_POINTS = 20;
    private final static Integer FIVE_DAYS_STREAK_POINTS = 25;
    private final static Integer SIX_DAYS_STREAK_POINTS = 30;
    private final static Integer SEVEN_DAYS_STREAK_POINTS = 50;

    @Override
    public List<UserAppCheckInDto> getUserAppCheckIn(String filter, String startDate, String endDate) {
        List<UserAppCheckInDto> userAppCheckInDtoList = new ArrayList<>();

        if (filter.equals("today")) {
            List<CheckInLog> checkInLogs = checkInLogRepository.findAll();

            for (CheckInLog checkInLog : checkInLogs) {
                UserAppCheckInDto userAppCheckInDto = modelMapperUtility
                        .initialize().map(checkInLog, UserAppCheckInDto.class);

                UserDetails userDetails = userDetailsRepository.findByUser(checkInLog.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                CheckInCounter checkInCounter = checkInCounterRepository.findByUser(checkInLog.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("Check in counter not found"));

                ZonedDateTime zonedDateTime = checkInLog.getCreatedDate().atZone(ZoneId.of("Asia/Jakarta"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String hour, minute;

                if (zonedDateTime.getHour() < 10) {
                    hour = "0" + zonedDateTime.getHour();
                } else {
                    hour = Integer.toString(zonedDateTime.getHour());
                }

                if (zonedDateTime.getMinute() < 10) {
                    minute = "0" + zonedDateTime.getMinute();
                } else {
                    minute = Integer.toString(zonedDateTime.getMinute());
                }

                userAppCheckInDto.setCid(userDetails.getJalinId());
                userAppCheckInDto.setName(userDetails.getFullName());

                switch (checkInCounter.getCounter()) {
                    case 1:
                        userAppCheckInDto.setPointsUnlocked(ONE_DAY_STREAK_POINTS);
                        break;
                    case 2:
                        userAppCheckInDto.setPointsUnlocked(TWO_DAYS_STREAK_POINTS);
                        break;
                    case 3:
                        userAppCheckInDto.setPointsUnlocked(THREE_DAYS_STREAK_POINTS);
                        break;
                    case 4:
                        userAppCheckInDto.setPointsUnlocked(FOUR_DAYS_STREAK_POINTS);
                        break;
                    case 5:
                        userAppCheckInDto.setPointsUnlocked(FIVE_DAYS_STREAK_POINTS);
                        break;
                    case 6:
                        userAppCheckInDto.setPointsUnlocked(SIX_DAYS_STREAK_POINTS);
                        break;
                    case 7:
                        userAppCheckInDto.setPointsUnlocked(SEVEN_DAYS_STREAK_POINTS);
                        break;
                }

                userAppCheckInDto.setDate(zonedDateTime.toLocalDate().format(dtf));
                userAppCheckInDto.setTimeMark(hour + "." + minute);

                userAppCheckInDtoList.add(userAppCheckInDto);
            }
        } else if (filter.equals("all") && startDate.equals("all") || endDate.equals("all")) {
            List<CheckIn> checkIns = checkInRepository.findAll();

            for (CheckIn checkIn : checkIns) {
                UserAppCheckInDto userAppCheckInDto = modelMapperUtility
                        .initialize().map(checkIn, UserAppCheckInDto.class);

                UserDetails userDetails = userDetailsRepository.findByUser(checkIn.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                CheckInCounter checkInCounter = checkInCounterRepository.findByUser(checkIn.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("Check in counter not found"));

                ZonedDateTime zonedDateTime = checkIn.getCreatedDate().atZone(ZoneId.of("Asia/Jakarta"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String hour, minute;

                if (zonedDateTime.getHour() < 10) {
                    hour = "0" + zonedDateTime.getHour();
                } else {
                    hour = Integer.toString(zonedDateTime.getHour());
                }

                if (zonedDateTime.getMinute() < 10) {
                    minute = "0" + zonedDateTime.getMinute();
                } else {
                    minute = Integer.toString(zonedDateTime.getMinute());
                }

                userAppCheckInDto.setCid(userDetails.getJalinId());
                userAppCheckInDto.setName(userDetails.getFullName());

                switch (checkInCounter.getCounter()) {
                    case 1:
                        userAppCheckInDto.setPointsUnlocked(ONE_DAY_STREAK_POINTS);
                        break;
                    case 2:
                        userAppCheckInDto.setPointsUnlocked(TWO_DAYS_STREAK_POINTS);
                        break;
                    case 3:
                        userAppCheckInDto.setPointsUnlocked(THREE_DAYS_STREAK_POINTS);
                        break;
                    case 4:
                        userAppCheckInDto.setPointsUnlocked(FOUR_DAYS_STREAK_POINTS);
                        break;
                    case 5:
                        userAppCheckInDto.setPointsUnlocked(FIVE_DAYS_STREAK_POINTS);
                        break;
                    case 6:
                        userAppCheckInDto.setPointsUnlocked(SIX_DAYS_STREAK_POINTS);
                        break;
                    case 7:
                        userAppCheckInDto.setPointsUnlocked(SEVEN_DAYS_STREAK_POINTS);
                        break;
                }

                userAppCheckInDto.setDate(zonedDateTime.toLocalDate().format(dtf));
                userAppCheckInDto.setTimeMark(hour + "." + minute);

                userAppCheckInDtoList.add(userAppCheckInDto);
            }

        } else if (filter.equals("all") && !startDate.equals("all") && !endDate.equals("all")) {
            LocalDate startDateLocal = LocalDate.parse(startDate);
            LocalDate endDateLocal = LocalDate.parse(endDate).plusDays(1);
            Instant startDateInstant = startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant endDateInstant = endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
            List<CheckIn> checkIns = checkInRepository
                    .findCheckInByCreatedDateBetween(startDateInstant, endDateInstant);

            for (CheckIn checkIn : checkIns) {
                UserAppCheckInDto userAppCheckInDto = modelMapperUtility
                        .initialize().map(checkIn, UserAppCheckInDto.class);

                UserDetails userDetails = userDetailsRepository.findByUser(checkIn.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                CheckInCounter checkInCounter = checkInCounterRepository.findByUser(checkIn.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("Check in counter not found"));

                ZonedDateTime zonedDateTime = checkIn.getCreatedDate().atZone(ZoneId.of("Asia/Jakarta"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String hour, minute;

                if (zonedDateTime.getHour() < 10) {
                    hour = "0" + zonedDateTime.getHour();
                } else {
                    hour = Integer.toString(zonedDateTime.getHour());
                }

                if (zonedDateTime.getMinute() < 10) {
                    minute = "0" + zonedDateTime.getMinute();
                } else {
                    minute = Integer.toString(zonedDateTime.getMinute());
                }

                userAppCheckInDto.setCid(userDetails.getJalinId());
                userAppCheckInDto.setName(userDetails.getFullName());

                switch (checkInCounter.getCounter()) {
                    case 1:
                        userAppCheckInDto.setPointsUnlocked(ONE_DAY_STREAK_POINTS);
                        break;
                    case 2:
                        userAppCheckInDto.setPointsUnlocked(TWO_DAYS_STREAK_POINTS);
                        break;
                    case 3:
                        userAppCheckInDto.setPointsUnlocked(THREE_DAYS_STREAK_POINTS);
                        break;
                    case 4:
                        userAppCheckInDto.setPointsUnlocked(FOUR_DAYS_STREAK_POINTS);
                        break;
                    case 5:
                        userAppCheckInDto.setPointsUnlocked(FIVE_DAYS_STREAK_POINTS);
                        break;
                    case 6:
                        userAppCheckInDto.setPointsUnlocked(SIX_DAYS_STREAK_POINTS);
                        break;
                    case 7:
                        userAppCheckInDto.setPointsUnlocked(SEVEN_DAYS_STREAK_POINTS);
                        break;
                }

                userAppCheckInDto.setDate(zonedDateTime.toLocalDate().format(dtf));
                userAppCheckInDto.setTimeMark(hour + "." + minute);

                userAppCheckInDtoList.add(userAppCheckInDto);
            }
        }
        return userAppCheckInDtoList;
    }
}
