package com.jalin.jalinappbackend.module.authentication.presenter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    private String mobileNumber;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String idCardNumber;
    @NotBlank
    private String fullName;
    @NotBlank
    private String dateOfBirth;
    @NotBlank
    private String address;
    @NotBlank
    private String province;
    @NotBlank
    private String city;
    @NotBlank
    private String subDistrict;
    @NotBlank
    private String postalCode;

    @NotBlank
    private String maritalStatus;
    @NotBlank
    private String bankingGoals;
    @NotBlank
    private String occupation;
    @NotBlank
    private String sourceOfIncome;
    @NotBlank
    private String incomeRange;
}
