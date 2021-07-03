package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDto;
import com.jalin.jalinappbackend.module.banking.model.CorporateDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.presenter.model.ConfirmPaymentRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.PaymentRequest;
import com.jalin.jalinappbackend.module.banking.service.CorporateService;
import com.jalin.jalinappbackend.module.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/banking")
public class PaymentController {
    @Autowired
    private CorporateService corporateService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment/qr/{corporateId}")
    public ResponseEntity<Object> getCorporateWithQr(@PathVariable String corporateId) {
        CorporateDto corporateDto = corporateService.getCorporateByCorporateId(corporateId);
        return new ResponseEntity<>(corporateDto, HttpStatus.OK);
    }

    @PostMapping(
            path = "/payment/qr",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> payWithQr(@Valid @ModelAttribute PaymentRequest requestBody) {
        TransactionDto transactionDto = paymentService.payWithQr(
                requestBody.getCorporateId(),
                requestBody.getAmount(),
                requestBody.getTransactionNote());
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/payment/qr/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> confirmPayWithQr(@Valid @ModelAttribute ConfirmPaymentRequest requestBody) {
        ConfirmPaymentDto confirmPayment = paymentService.confirmPayment(
                requestBody.getCorporateId(),
                requestBody.getAmount());
        return new ResponseEntity<>(confirmPayment, HttpStatus.OK);
    }

    @GetMapping("/payment/corporates/merchant")
    public ResponseEntity<Object> getMerchantCorporates() {
        List<CorporateDto> corporateDtoList = corporateService.getMerchantCorporates();
        return new ResponseEntity<>(corporateDtoList, HttpStatus.OK);
    }
}
