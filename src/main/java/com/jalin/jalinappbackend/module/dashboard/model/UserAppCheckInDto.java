package com.jalin.jalinappbackend.module.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAppCheckInDto {
    private String cid;
    private String name;
    private Integer pointsUnlocked;
    private String date;
    private String timeMark;
}
