package com.jalin.jalinappbackend.module.gamification.point.entity;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "points")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Point {
    @Id
    private UUID pointId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;
    private Integer total;

    @NaturalId
    @Column(name = "point_detail_id")
    private UUID pointDetailId;

    public Point(User userId, Integer total, UUID pointDetailId) {
        this.userId = userId;
        this.total = total;
        this.pointDetailId = pointDetailId;
    }
}
