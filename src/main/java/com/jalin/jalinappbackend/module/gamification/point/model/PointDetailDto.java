package com.jalin.jalinappbackend.module.gamification.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PointDetailDto {
    private UUID pointDetailId;
    private String pointType;
    private Integer pointAmount;
    private String sourceName;
    private Instant createdDate;
}
