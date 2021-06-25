package com.jalin.jalinappbackend.module.banking.service;

import com.jalin.jalinappbackend.exception.AddWalletListFailedException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.banking.entity.WalletList;
import com.jalin.jalinappbackend.module.banking.model.WalletListDto;
import com.jalin.jalinappbackend.module.banking.repository.WalletListRepository;
import com.jalin.jalinappbackend.utility.FakerUtility;
import com.jalin.jalinappbackend.utility.ModelMapperUtility;
import com.jalin.jalinappbackend.utility.RestTemplateUtility;
import com.jalin.jalinappbackend.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletListServiceImpl implements WalletListService {
    @Autowired
    private FakerUtility fakerUtility;
    @Autowired
    private ModelMapperUtility modelMapperUtility;
    @Autowired
    private RestTemplateUtility restTemplateUtility;
    @Autowired
    private UserUtility userUtility;
    @Autowired
    private WalletListRepository walletListRepository;
    @Autowired
    private CorporateService corporateService;

    @Override
    public List<WalletListDto> getWalletList() {
        User user = userUtility.getSignedInUser();
        List<WalletList> walletListFound = walletListRepository.findByUser(user);
        List<WalletListDto> walletListDto = new ArrayList<>();
        for (WalletList walletList : walletListFound) {
            WalletListDto walletListDtoMapped = modelMapperUtility.initialize()
                    .map(walletList, WalletListDto.class);
            walletListDtoMapped.setCorporateName(
                    corporateService.getCorporateByCorporateId(walletList.getCorporateId()).getCorporateName());
            walletListDto.add(walletListDtoMapped);
        }
        return walletListDto;
    }

    @Override
    public WalletListDto addWalletList(String corporateId, String beneficiaryAccountNumber) {
        User user = userUtility.getSignedInUser();
        validateWalletList(user, corporateId, beneficiaryAccountNumber);

        WalletList walletList = new WalletList();
        walletList.setCorporateId(corporateId);
        walletList.setAccountNumber(beneficiaryAccountNumber);
        walletList.setFullName(fakerUtility.initialize().name().fullName());
        walletList.setUser(user);
        WalletList savedWalledList = walletListRepository.save(walletList);

        WalletListDto walletListDto = modelMapperUtility.initialize()
                .map(walletListRepository.save(walletList), WalletListDto.class);
        walletListDto.setCorporateName(
                corporateService.getCorporateByCorporateId(savedWalledList.getCorporateId()).getCorporateName());
        return walletListDto;
    }

    private void validateWalletList(User user, String corporateId, String beneficiaryAccountNumber) {
        corporateService.getCorporateByCorporateId(corporateId);
        if (walletListRepository.findByUserAndCorporateIdAndAccountNumber(user, corporateId, beneficiaryAccountNumber).isPresent()) {
            throw new AddWalletListFailedException("The digital wallet account is already added in wallet list");
        }
    }
}
