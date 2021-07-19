package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.CustomerIdNotValidException;
import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;
import com.jalin.jalinappbackend.module.banking.service.model.payment.GetPrepaidOptionResponse;
import com.jalin.jalinappbackend.module.banking.service.model.payment.PrepaidOption;
import com.jalin.jalinappbackend.utility.FakerUtility;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentBillServiceImpl implements PaymentBillService {
    @Value("${resource.server.url}")
    private String BASE_URL;
    private static final String GET_ELECTRICITY_PREPAID_OPTIONS = "/api/v1/prepaid/electricity";
    private static final String GET_ELECTRICITY_PREPAID_OPTION_BY_ID = "/api/v1/prepaid/electricity/";
    private static final String PLN_CORPORATE_ID = "10123";

    private static final BigDecimal IDR_NO_PAYMENT_FEE = new BigDecimal("0").setScale(2, RoundingMode.UNNECESSARY);
    private static final BigDecimal IDR_NO_PAYMENT_DISCOUNT = new BigDecimal("0").setScale(2, RoundingMode.UNNECESSARY);

    @Autowired
    private FakerUtility fakerUtility;
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;

    @Autowired
    private CorporateService corporateService;

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

    @Override
    public ConfirmPaymentDetailsDto confirmPaymentElectricityPrepaid(String customerId, UUID prepaidId) {
        validateCustomerId(customerId);

        try {
            ResponseEntity<PrepaidOption> responsePrepaid = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_ELECTRICITY_PREPAID_OPTION_BY_ID + prepaidId,
                    PrepaidOption.class);

            return initializeConfirmPaymentDetailsDto(
                    PLN_CORPORATE_ID,
                    corporateService.getCorporateByCorporateId(PLN_CORPORATE_ID).getCorporateName(),
                    Objects.requireNonNull(responsePrepaid.getBody()).getPrice(),
                    IDR_NO_PAYMENT_FEE,
                    IDR_NO_PAYMENT_DISCOUNT,
                    responsePrepaid.getBody().getPrepaidName());
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new ResourceNotFoundException(error);
        }
    }

    private void validateCustomerId(String customerId) {
        if (!customerId.matches("^[0-9]*$") | (customerId.length() != 12)) {
            throw new CustomerIdNotValidException("Customer ID not valid");
        }
    }

    private ConfirmPaymentDetailsDto initializeConfirmPaymentDetailsDto(
            String corporateId,
            String corporateName,
            BigDecimal transferAmount,
            BigDecimal transferFee,
            BigDecimal transferDiscount,
            Object transferDetails) {
        return new ConfirmPaymentDetailsDto(
                corporateId,
                corporateName,
                transferAmount,
                transferFee,
                transferDiscount,
                transferAmount.add(transferFee).subtract(transferDiscount),
                transferDetails);
    }
}
