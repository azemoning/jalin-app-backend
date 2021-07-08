package com.jalin.jalinappbackend.module.gamification.voucher.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vouchers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue
    private UUID id;
    private String category;
    private String usage;
    private Integer quota;

    @Column(name = "tnc_description")
    private String tncDescription;
    private Integer points;
    private LocalDate validity;
}
