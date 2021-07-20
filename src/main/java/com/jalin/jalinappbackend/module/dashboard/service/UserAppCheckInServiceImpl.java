package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.dashboard.model.UserAppCheckInDto;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInRepository;
import com.jalin.jalinappbackend.module.gamification.point.entity.PointSource;
import com.jalin.jalinappbackend.module.gamification.point.repository.PointSourceRepository;
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
    private CheckInRepository checkInRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PointSourceRepository pointSourceRepository;

    @Override
    public List<UserAppCheckInDto> getUserAppCheckIn(String filter, String startDate, String endDate) {
        List<UserAppCheckInDto> userAppCheckInDtoList = new ArrayList<>();

        if (filter.equals("today")) {
            LocalDate startLocalDate = LocalDate.now();
            LocalDate endLocalDate = startLocalDate.plusDays(1);
            Instant startInstant = startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant endInstant = endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

            List<CheckIn> checkIns = checkInRepository.findTop4ByCreatedDateBetweenOrderByCreatedDateDesc(startInstant, endInstant);

            for (CheckIn checkIn : checkIns) {
                UserDetails userDetails = userDetailsRepository.findByUser(checkIn.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                ZonedDateTime zonedDateTime = checkIn.getCreatedDate().atZone(ZoneId.of("Asia/Jakarta"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                userAppCheckInDtoList.add(new UserAppCheckInDto(
                        userDetails.getJalinId(),
                        userDetails.getFullName(),
                        getPointByCheckIn(checkIn),
                        zonedDateTime.toLocalDate().format(dtf),
                        getTimeMarkByZonedDateTime(zonedDateTime)
                ));
            }
        } else if (filter.equals("all") && startDate.equals("all") || endDate.equals("all")) {
            List<CheckIn> checkIns = checkInRepository.findAllByOrderByCreatedDateDesc();

            for (CheckIn checkIn : checkIns) {
                UserDetails userDetails = userDetailsRepository.findByUser(checkIn.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                ZonedDateTime zonedDateTime = checkIn.getCreatedDate().atZone(ZoneId.of("Asia/Jakarta"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                userAppCheckInDtoList.add(new UserAppCheckInDto(
                        userDetails.getJalinId(),
                        userDetails.getFullName(),
                        getPointByCheckIn(checkIn),
                        zonedDateTime.toLocalDate().format(dtf),
                        getTimeMarkByZonedDateTime(zonedDateTime)
                ));
            }
        } else if (filter.equals("all") && !startDate.equals("all") && !endDate.equals("all")) {
            LocalDate startDateLocal = LocalDate.parse(startDate);
            LocalDate endDateLocal = LocalDate.parse(endDate).plusDays(1);
            Instant startDateInstant = startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant endDateInstant = endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
            List<CheckIn> checkIns = checkInRepository
                    .findCheckInByCreatedDateBetweenOrderByCreatedDateDesc(startDateInstant, endDateInstant);

            for (CheckIn checkIn : checkIns) {
                UserDetails userDetails = userDetailsRepository.findByUser(checkIn.getUser())
                        .orElseThrow(() -> new ResourceNotFoundException("User details not found"));

                ZonedDateTime zonedDateTime = checkIn.getCreatedDate().atZone(ZoneId.of("Asia/Jakarta"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                userAppCheckInDtoList.add(new UserAppCheckInDto(
                        userDetails.getJalinId(),
                        userDetails.getFullName(),
                        getPointByCheckIn(checkIn),
                        zonedDateTime.toLocalDate().format(dtf),
                        getTimeMarkByZonedDateTime(zonedDateTime)
                ));
            }
        }
        return userAppCheckInDtoList;
    }

    private Integer getPointByCheckIn(CheckIn checkIn) {
        PointSource pointSource = pointSourceRepository.findByCheckInCheckInId(checkIn.getCheckInId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Point source with check-in ID %s not found", checkIn.getCheckInId())));
        return pointSource.getPointDetail().getPointAmount();
    }

    private String getTimeMarkByZonedDateTime(ZonedDateTime zonedDateTime) {
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

        return hour + "." + minute;
    }
}
