package com.jalin.jalinappbackend.module.dashboard.entity;

    import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "calendar_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalendarData {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDate date;
    private Integer totalCheckIn;
    private Integer totalMissionCompleted;
}
