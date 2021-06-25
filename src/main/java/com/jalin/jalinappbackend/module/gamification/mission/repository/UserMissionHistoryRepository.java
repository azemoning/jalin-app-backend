package com.jalin.jalinappbackend.module.gamification.mission.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserMissionHistoryRepository extends JpaRepository<UserMissionHistory, UUID> {
    UserMissionHistory findUserMissionHistoryByUserAndMission(User user, Mission mission);
    List<UserMissionHistory> findUserMissionHistoriesByUser(User user);
}
