package com.jalin.jalinappbackend.module.gamification.point.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "point_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PointDetail {
    @Id
    @GeneratedValue
    private UUID pointDetailId;

    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;
    private Integer pointAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @CreationTimestamp
    private Instant createdDate;
}
