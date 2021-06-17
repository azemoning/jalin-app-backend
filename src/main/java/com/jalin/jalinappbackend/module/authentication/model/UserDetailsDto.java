package com.jalin.jalinappbackend.module.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsDto {
    private String fullName;
    private String email;
    private String accountNumber;
    private BigDecimal balance;
    private String jalinId;
    private String displayName;
    private String mobileNumber;
    private String profilePicture;
    private String idCardImage;
    private String npwpImage;
}
