package com.jalin.jalinappbackend.module.gamification.point.entity;

import com.jalin.jalinappbackend.module.gamification.checkin.entity.CheckIn;
import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "point_sources")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PointSource {
    @Id
    private UUID pointSourceId;

    @Enumerated(EnumType.STRING)
    private PointSourceEnum sourceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_in_id")
    private CheckIn checkIn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_detail_id")
    private PointDetail pointDetail;

    @CreationTimestamp
    private Instant createdDate;
}
