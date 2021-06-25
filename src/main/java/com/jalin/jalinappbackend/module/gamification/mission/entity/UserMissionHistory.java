package com.jalin.jalinappbackend.module.gamification.mission.entity;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_mission_histories")
@Data
public class UserMissionHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "user_mission_id")
    private UserMission userMission;
}
