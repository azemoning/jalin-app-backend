package com.jalin.jalinappbackend.module.gamification.mission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "missions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Mission {
    @Id
    @GeneratedValue
    private UUID id;
    private String activity;

    @Column(name = "mission_description")
    private String missionDescription;

    @Column(name = "tnc_description")
    private String tncDescription;
    private Integer frequency;

    @Column(name = "minimum_amount")
    private BigDecimal minAmount;
    private String expiration;
    private Integer point;
}
