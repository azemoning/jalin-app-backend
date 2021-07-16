package com.jalin.jalinappbackend.module.gamification.mission.repository;

import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MissionRepository extends JpaRepository<Mission, UUID> {
    List<Mission> findMissionsByExpirationEqualsAndStatusEquals(String expiration, Boolean status);
}
