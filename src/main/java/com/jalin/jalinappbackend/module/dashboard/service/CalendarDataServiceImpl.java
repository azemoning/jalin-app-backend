package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.dashboard.entity.CalendarData;
import com.jalin.jalinappbackend.module.dashboard.model.CalendarDataDto;
import com.jalin.jalinappbackend.module.dashboard.repository.CalendarDataRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckInLog;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInLogRepository;
import com.jalin.jalinappbackend.module.gamification.checkin.repository.CheckInRepository;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import com.jalin.jalinappbackend.module.gamification.mission.model.UserMissionDto;
import com.jalin.jalinappbackend.module.gamification.mission.repository.UserMissionRepository;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarDataServiceImpl implements CalendarDataService {

    @Autowired
    private CheckInLogRepository checkInLogRepository;

    @Autowired
    private UserMissionRepository userMissionRepository;

    @Autowired
    private CalendarDataRepository calendarDataRepository;

    @Autowired
    private ModelMapperUtility modelMapperUtility;

    @Override
    public List<CalendarDataDto> getCalendarData() {
        List<CalendarData> calendarDataList = calendarDataRepository.findAll();
        List<CalendarDataDto> calendarDataDtoList = new ArrayList<>();

        for (CalendarData calendarData : calendarDataList) {
            CalendarDataDto calendarDataCheckIn = modelMapperUtility
                    .initialize().map(calendarData, CalendarDataDto.class);

            CalendarDataDto calendarDataMission = modelMapperUtility
                    .initialize().map(calendarData, CalendarDataDto.class);


            // Check In
            calendarDataCheckIn.setTitle(calendarData.getTotalCheckIn().toString() + " Checkins");
            calendarDataCheckIn.setStart(calendarData.getDate().toString());
            calendarDataCheckIn.setEnd(calendarData.getDate().toString());
            calendarDataCheckIn.setHexColor("#3EB7A1");

            calendarDataDtoList.add(calendarDataCheckIn);

            // Mission
            calendarDataMission.setTitle(calendarData.getTotalMissionCompleted().toString() + " Missions");
            calendarDataMission.setStart(calendarData.getDate().toString());
            calendarDataMission.setEnd(calendarData.getDate().toString());
            calendarDataMission.setHexColor("#E15E54");

            calendarDataDtoList.add(calendarDataMission);
        }

        return calendarDataDtoList;
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT+7.00")
    private void saveCalendarData() {
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalDate today = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));

        CalendarData calendarData = new CalendarData();

        calendarData.setDate(today);
        calendarData.setTotalCheckIn(0);
        calendarData.setTotalMissionCompleted(0);
        calendarDataRepository.save(calendarData);
    }

    @Override
    @Scheduled(cron = "0 0/45 * * * ?", zone = "GMT+7.00")
    public void updateCalendarData() {
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
        LocalDate today = LocalDate.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));

        Optional<CalendarData> calendarData = calendarDataRepository.getCalendarDataByDateEquals(today);

        if (calendarData.isEmpty()) {
            saveCalendarData();
        } else {
            List<CheckInLog> checkInLogs = checkInLogRepository.findAll();
            List<UserMission> userMissions = userMissionRepository
                    .findUserMissionsByStatusAndCompletionDate("COMPLETED", today);

            CalendarData data = calendarData.get();

            data.setTotalCheckIn(checkInLogs.size());
            data.setTotalMissionCompleted(userMissions.size());

            calendarDataRepository.save(data);
        }
    }
}
