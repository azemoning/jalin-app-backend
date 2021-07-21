package com.jalin.jalinappbackend.module.gamification.voucher.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private String category;

    @NotNull
    private String usage;

    @NotNull
    private Integer amount = 0;

    @NotNull
    private Integer quota;

    @Column(name = "tnc_description")
    @NotNull
    private String tncDescription;

    @NotNull
    private Integer points;

    @NotNull
    private LocalDate validity;

    @NotNull
    private Boolean status = true;
}
