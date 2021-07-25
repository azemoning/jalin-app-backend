package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.banking.model.CorporateDto;
import com.jalin.jalinappbackend.module.banking.service.model.corporate.CorporateResponse;
import com.jalin.jalinappbackend.module.banking.service.model.corporate.GetCorporateByIdResponse;
import com.jalin.jalinappbackend.module.banking.service.model.corporate.GetCorporatesResponse;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CorporateServiceImpl implements CorporateService {
    @Value("${resource.server.url}")
    private String BASE_URL;

    private static final String GET_BANK_CORPORATES_ENDPOINT = "/v1/corporates/bank";
    private static final String GET_DIGITAL_WALLET_CORPORATES_ENDPOINT = "/v1/corporates/wallet";
    private static final String GET_MERCHANT_CORPORATES_ENDPOINT = "/v1/corporates/merchant";
    private static final String GET_CORPORATE_BY_CORPORATE_ID = "/v1/corporates/";
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;

    @Override
    public List<CorporateDto> getBankCorporates() {
        ResponseEntity<GetCorporatesResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_BANK_CORPORATES_ENDPOINT,
                GetCorporatesResponse.class);

        List<CorporateResponse> corporateResponseList = Objects.requireNonNull(response.getBody()).getCorporateList();
        List<CorporateDto> corporateDtoList = new ArrayList<>();
        for (CorporateResponse corporateResponse : corporateResponseList) {
            CorporateDto corporateDto = modelMapperUtility.initialize()
                    .map(corporateResponse, CorporateDto.class);
            corporateDtoList.add(corporateDto);
        }
        return corporateDtoList;
    }

    @Override
    public List<CorporateDto> getDigitalWalletCorporates() {
        ResponseEntity<GetCorporatesResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_DIGITAL_WALLET_CORPORATES_ENDPOINT,
                GetCorporatesResponse.class);

        List<CorporateResponse> corporateResponseList = Objects.requireNonNull(response.getBody()).getCorporateList();
        List<CorporateDto> corporateDtoList = new ArrayList<>();
        for (CorporateResponse corporateResponse : corporateResponseList) {
            CorporateDto corporateDto = modelMapperUtility.initialize()
                    .map(corporateResponse, CorporateDto.class);
            corporateDtoList.add(corporateDto);
        }
        return corporateDtoList;
    }

    @Override
    public List<CorporateDto> getMerchantCorporates() {
        ResponseEntity<GetCorporatesResponse> response = restTemplateUtility.initialize().getForEntity(
                BASE_URL + GET_MERCHANT_CORPORATES_ENDPOINT,
                GetCorporatesResponse.class);

        List<CorporateResponse> corporateResponseList = Objects.requireNonNull(response.getBody()).getCorporateList();
        List<CorporateDto> corporateDtoList = new ArrayList<>();
        for (CorporateResponse corporateResponse : corporateResponseList) {
            CorporateDto corporateDto = modelMapperUtility.initialize()
                    .map(corporateResponse, CorporateDto.class);
            corporateDtoList.add(corporateDto);
        }
        return corporateDtoList;
    }

    @Override
    public CorporateDto getCorporateByCorporateId(String corporateId) {
        try {
            ResponseEntity<GetCorporateByIdResponse> response = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + GET_CORPORATE_BY_CORPORATE_ID + corporateId,
                    GetCorporateByIdResponse.class);
            return modelMapperUtility.initialize()
                    .map(response.getBody(), CorporateDto.class);
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new ResourceNotFoundException(error);
        }
    }
}
