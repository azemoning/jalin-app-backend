package com.jalin.jalinappbackend.module.gamification.checkin.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckInCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckInCounterRepository extends JpaRepository<CheckInCounter, UUID> {
    Optional<CheckInCounter> findByUser(User user);
}
