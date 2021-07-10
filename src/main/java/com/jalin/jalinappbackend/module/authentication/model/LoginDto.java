package com.jalin.jalinappbackend.module.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDto {
    private String fullName;
    private String email;
    private String role;
    private String token;
}
