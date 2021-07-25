package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.AddTransferListFailedException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.banking.entity.TransferList;
import com.jalin.jalinappbackend.module.banking.model.TransferListDto;
import com.jalin.jalinappbackend.module.banking.repository.TransferListRepository;
import com.jalin.jalinappbackend.module.banking.service.model.GetCustomerFullNameResponse;
import com.jalin.jalinappbackend.utility.FakerUtility;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import com.jalin.jalinappbackend.utility.UserUtility;
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
public class TransferListServiceImpl implements TransferListService {
    @Value("${resource.server.url}")
    private String BASE_URL;

    private static final String FIND_CUSTOMER_ENDPOINT = "/v1/customers/find";
    private static final String FIND_CUSTOMER_ENDPOINT_ID_CARD_NUMBER_PARAMETER = "idCardNumber=";
    private static final String FIND_CUSTOMER_ENDPOINT_ACCOUNT_NUMBER_PARAMETER = "accountNumber=";
    @Autowired
    private FakerUtility fakerUtility;
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserUtility userUtility;
    @Autowired
    private TransferListRepository transferListRepository;
    @Autowired
    private CorporateService corporateService;

    @Override
    public List<TransferListDto> getTransferList() {
        User user = userUtility.getSignedInUser();
        List<TransferList> transferListFound = transferListRepository.findByUser(user);
        List<TransferListDto> transferListDto = new ArrayList<>();
        for (TransferList transferList : transferListFound) {
            TransferListDto transferListDtoMapped = modelMapperUtility.initialize()
                    .map(transferList, TransferListDto.class);
            transferListDtoMapped.setCorporateName(
                    corporateService.getCorporateByCorporateId(transferList.getCorporateId()).getCorporateName());
            transferListDto.add(transferListDtoMapped);
        }
        return transferListDto;
    }

    @Override
    public TransferListDto addTransferList(String corporateId, String beneficiaryAccountNumber) {
        User user = userUtility.getSignedInUser();
        validateTransferList(user, corporateId, beneficiaryAccountNumber);

        if (!corporateId.equals("212")) {
            TransferList transferList = new TransferList();
            transferList.setCorporateId(corporateId);
            transferList.setAccountNumber(beneficiaryAccountNumber);
            transferList.setFullName(fakerUtility.initialize().name().fullName());
            transferList.setUser(user);
            TransferList savedTransferList = transferListRepository.save(transferList);

            TransferListDto transferListDto = modelMapperUtility.initialize()
                    .map(savedTransferList, TransferListDto.class);
            transferListDto.setCorporateName(
                    corporateService.getCorporateByCorporateId(savedTransferList.getCorporateId()).getCorporateName());
            return transferListDto;
        } else {
            TransferList transferList = new TransferList();
            transferList.setCorporateId(corporateId);
            transferList.setAccountNumber(beneficiaryAccountNumber);
            transferList.setFullName(getCustomerFullName(beneficiaryAccountNumber));
            transferList.setUser(user);
            TransferList savedTransferList = transferListRepository.save(transferList);

            TransferListDto transferListDto = modelMapperUtility.initialize()
                    .map(savedTransferList, TransferListDto.class);
            transferListDto.setCorporateName(
                    corporateService.getCorporateByCorporateId(savedTransferList.getCorporateId()).getCorporateName());
            return transferListDto;
        }
    }

    private String getCustomerFullName(String accountNumber) {
        try {
            ResponseEntity<GetCustomerFullNameResponse> response = restTemplateUtility.initialize().getForEntity(
                    BASE_URL + FIND_CUSTOMER_ENDPOINT + "?" +
                            FIND_CUSTOMER_ENDPOINT_ID_CARD_NUMBER_PARAMETER + "&" +
                            FIND_CUSTOMER_ENDPOINT_ACCOUNT_NUMBER_PARAMETER + accountNumber,
                    GetCustomerFullNameResponse.class);
            return Objects.requireNonNull(response.getBody()).getFullName();
        } catch (HttpClientErrorException exception) {
            JSONObject object = new JSONObject(exception.getResponseBodyAsString());
            String error = object.getString("error");
            throw new AddTransferListFailedException(error);
        }
    }

    private void validateTransferList(User user, String corporateId, String beneficiaryAccountNumber) {
        corporateService.getCorporateByCorporateId(corporateId);
        if (transferListRepository.findByUserAndCorporateIdAndAccountNumber(user, corporateId, beneficiaryAccountNumber).isPresent()) {
            throw new AddTransferListFailedException("The bank account is already added in transfer list");
        }
    }
}
