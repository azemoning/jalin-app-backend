package com.jalin.jalinappbackend.module.gamification.checkin.entity;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "check_in")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckIn {
    @Id
    @GeneratedValue
    private UUID checkInId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    private Instant createdDate;
}
