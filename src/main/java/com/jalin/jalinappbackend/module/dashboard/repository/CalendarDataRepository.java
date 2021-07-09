package com.jalin.jalinappbackend.module.dashboard.repository;

import com.jalin.jalinappbackend.module.dashboard.entity.CalendarData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalendarDataRepository extends JpaRepository<CalendarData, UUID> {
    Optional<CalendarData> getCalendarDataByDateEquals(LocalDate date);
}
