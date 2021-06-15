package com.jalin.jalinappbackend.module.authentication.presenter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
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
