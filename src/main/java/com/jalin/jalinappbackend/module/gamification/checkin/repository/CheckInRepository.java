package com.jalin.jalinappbackend.module.gamification.checkin.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, UUID> {
    Optional<CheckIn> findFirstByUserOrderByCreatedDateDesc(User user);
    List<CheckIn> findCheckInByCreatedDateBetweenOrderByCreatedDateDesc(Instant startDate, Instant endDate);
    List<CheckIn> findAllByOrderByCreatedDateDesc();
    List<CheckIn> findTop4ByCreatedDateBetweenOrderByCreatedDateDesc(Instant startDate, Instant endDate);
}
