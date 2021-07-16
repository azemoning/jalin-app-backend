package com.jalin.jalinappbackend.module.gamification.mission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private String activity;

    @Column(name = "mission_description")
    @NotNull
    private String missionDescription;

    @Column(name = "tnc_description")
    @NotNull
    private String tncDescription;

    @NotNull
    private Integer frequency;

    @Column(name = "minimum_amount")
    @NotNull
    private BigDecimal minAmount;

    @NotNull
    private String expiration;

    @NotNull
    private Integer point;

    @NotNull
    private Boolean status = true;
}
