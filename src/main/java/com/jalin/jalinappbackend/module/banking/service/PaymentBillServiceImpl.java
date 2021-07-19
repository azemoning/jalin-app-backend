package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.CustomerIdNotValidException;
import com.jalin.jalinappbackend.module.banking.model.PrepaidDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;
import com.jalin.jalinappbackend.module.banking.service.model.payment.GetPrepaidOptionResponse;
import com.jalin.jalinappbackend.module.banking.service.model.payment.PrepaidOption;
import com.jalin.jalinappbackend.utility.FakerUtility;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentBillServiceImpl implements PaymentBillService {
    @Value("${resource.server.url}")
    private String BASE_URL;
    private static final String GET_ELECTRICITY_PREPAID_OPTIONS = "/api/v1/prepaid/electricity";

    @Autowired
    private FakerUtility fakerUtility;
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;

    @Override
    public PrepaidElectricityDto getElectricityPrepaidOptions(String customerId) {
        validateCustomerId(customerId);

        ResponseEntity<GetPrepaidOptionResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_ELECTRICITY_PREPAID_OPTIONS,
                GetPrepaidOptionResponse.class);

        List<PrepaidDto> prepaidDtoList = new ArrayList<>();
        for (PrepaidOption prepaidOption : Objects.requireNonNull(response.getBody()).getPrepaidOptionList()) {
            PrepaidDto prepaidDto = modelMapperUtility.initialize()
                    .map(prepaidOption, PrepaidDto.class);
            prepaidDtoList.add(prepaidDto);
        }

        PrepaidElectricityDto prepaidElectricityDto = new PrepaidElectricityDto();
        prepaidElectricityDto.setCustomerId(customerId);
        prepaidElectricityDto.setCustomerName(fakerUtility.initialize().name().fullName());
        prepaidElectricityDto.setTokenList(prepaidDtoList);
        return prepaidElectricityDto;
    }

    private void validateCustomerId(String customerId) {
        if (!customerId.matches("^[0-9]*$") | (customerId.length() != 12)) {
            throw new CustomerIdNotValidException("Customer ID not valid");
        }
    }
}
