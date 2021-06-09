package com.jalin.jalinappbackend.module.authentication.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddNewCustomerRequest {
    private String idCardNumber;
    private String fullName;
}
