package com.jalin.jalinappbackend.module.gamification.checkin.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckInLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckInLogRepository extends JpaRepository<CheckInLog, UUID> {
    Optional<CheckInLog> findByUser(User user);
    List<CheckInLog> findTop4ByOrderByCreatedDateDesc();
}
