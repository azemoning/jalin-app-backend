package com.jalin.jalinappbackend.module.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMissionCompletionDto {
    private String cid;
    private String fullName;
    private String missionTransaction;
    private Integer pointsUnlocked;
    private String date;
    private String timeMark;
    private String status;
}
