package com.jalin.jalinappbackend.module.gamification.point.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private UUID id;

    @ManyToOne()
    @JoinColumn(
            name = "point_detail_id",
            referencedColumnName = "point_detail_id"
    )
    private Point point;
    private Integer amount;

    @Column(name = "point_source")
    private String pointSource;

    public PointDetail(Point point, Integer amount, String pointSource) {
        this.point = point;
        this.amount = amount;
        this.pointSource = pointSource;
    }

    public PointDetail(UUID pointDetailId) {
    }
}
