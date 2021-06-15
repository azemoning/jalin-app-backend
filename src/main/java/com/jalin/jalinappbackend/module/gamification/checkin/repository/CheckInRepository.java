package com.jalin.jalinappbackend.module.gamification.checkin.repository;

import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, UUID> {
}
