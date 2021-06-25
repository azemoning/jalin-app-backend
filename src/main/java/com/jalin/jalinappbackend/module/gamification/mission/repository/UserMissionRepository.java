package com.jalin.jalinappbackend.module.gamification.mission.repository;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.gamification.mission.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, UUID> {
    List<UserMission> findUserMissionByStatusEquals(String status);
    List<UserMission> findUserMissionsByUser(User user);
    List<UserMission> findUserMissionsByUserAndIsActiveEquals(User user, Boolean isActive);
}
