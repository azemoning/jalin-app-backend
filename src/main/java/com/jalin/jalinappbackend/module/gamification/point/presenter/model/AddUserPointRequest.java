package com.jalin.jalinappbackend.module.gamification.point.presenter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddUserPointRequest {
    @NotBlank
    private Integer amount;

    @NotBlank
    private String pointSource;
}
