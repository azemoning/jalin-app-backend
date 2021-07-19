package com.jalin.jalinappbackend.module.banking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrepaidMobilePhoneDto {
    private String providerId;
    private String productName;
    private String providerName;
    private String mobilePhoneNumber;
    private List<PrepaidDto> creditList;
    private List<PrepaidDetailsDto> dataList;
}
