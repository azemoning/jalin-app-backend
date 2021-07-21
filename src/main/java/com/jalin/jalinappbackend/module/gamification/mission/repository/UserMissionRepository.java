package com.jalin.jalinappbackend.module.gamification.mission.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, UUID> {
    List<UserMission> findUserMissionByUserAndStatusEquals(User user, String status);
    List<UserMission> findUserMissionsByStatusEqualsOrderByModifiedDateDesc(String status);
    List<UserMission> findUserMissionsByStatusEqualsAndModifiedDateBetweenOrderByModifiedDateDesc(String status, Instant startDate, Instant endDate);
    List<UserMission> findUserMissionsByUserAndIsActiveEquals(User user, Boolean isActive);
    List<UserMission> findUserMissionsByStatusAndCompletionDate(String status, LocalDate completionDate);
    List<UserMission> findUserMissionsByMission(Mission mission);
}
