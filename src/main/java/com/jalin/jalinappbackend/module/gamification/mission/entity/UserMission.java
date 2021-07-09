package com.jalin.jalinappbackend.module.gamification.mission.entity;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "user_missions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMission {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "mission_progress")
    private Integer missionProgress;
    private LocalDate completionDate;
    private LocalTime completionTime;
    private Boolean isClaimed;
    private Boolean isActive;
    private String status;

    @CreationTimestamp
    private Instant createdDate;

    @UpdateTimestamp
    private Instant modifiedDate;
}
