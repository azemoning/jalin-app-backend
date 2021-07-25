package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidMobilePhoneDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.presenter.model.ConfirmPaymentMobilePhoneRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.PaymentMobilePhoneRequest;
import com.jalin.jalinappbackend.module.banking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("${url.map.api}/v1/banking")
public class PaymentMobilePhoneController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("payment/mobile/prepaid/{mobileNumber}")
    public ResponseEntity<Object> getMobilePhonePrepaidOptions(@PathVariable String mobileNumber) {
        PrepaidMobilePhoneDto prepaidMobilePhoneDto = paymentService.getMobilePhonePrepaidOptions(mobileNumber);
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Mobile phone prepaid options successfully found", prepaidMobilePhoneDto),
                HttpStatus.OK);
    }

    @PostMapping(
            path = "payment/mobile/prepaid/{mobileNumber}/credit/pay",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> payMobilePhoneCredit(
            @PathVariable String mobileNumber,
            @Valid @ModelAttribute PaymentMobilePhoneRequest requestBody) {
        TransactionDto transactionDto = paymentService.payMobilePhoneCredit(
                requestBody.getProviderId(),
                mobileNumber,
                requestBody.getAmount());
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "payment/mobile/prepaid/{mobileNumber}/credit/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> confirmMobilePhoneCreditPayment(
            @PathVariable String mobileNumber,
            @Valid @ModelAttribute ConfirmPaymentMobilePhoneRequest requestBody) {
        ConfirmPaymentDetailsDto confirmPaymentDetailsDto = paymentService.confirmPaymentMobilePhoneCredit(
                requestBody.getProviderId(),
                UUID.fromString(requestBody.getPrepaidId()),
                mobileNumber);
        return new ResponseEntity<>(confirmPaymentDetailsDto, HttpStatus.OK);
    }

    @PostMapping(
            path = "payment/mobile/prepaid/{mobileNumber}/data/pay",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> payMobilePhoneData(
            @PathVariable String mobileNumber,
            @Valid @ModelAttribute PaymentMobilePhoneRequest requestBody) {
        TransactionDto transactionDto = paymentService.payMobilePhoneData(
                requestBody.getProviderId(),
                mobileNumber,
                requestBody.getAmount());
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "payment/mobile/prepaid/{mobileNumber}/data/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> confirmMobilePhoneDataPayment(
            @PathVariable String mobileNumber,
            @Valid @ModelAttribute ConfirmPaymentMobilePhoneRequest requestBody) {
     ConfirmPaymentDetailsDto confirmPaymentDetailsDto = paymentService.confirmPaymentMobilePhoneData(
             requestBody.getProviderId(),
             UUID.fromString(requestBody.getPrepaidId()),
             mobileNumber);
     return new ResponseEntity<>(confirmPaymentDetailsDto, HttpStatus.OK);
    }
}
