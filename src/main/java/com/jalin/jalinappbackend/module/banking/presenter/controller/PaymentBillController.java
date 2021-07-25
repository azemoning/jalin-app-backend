package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;
import com.jalin.jalinappbackend.module.banking.model.TransactionDto;
import com.jalin.jalinappbackend.module.banking.presenter.model.ConfirmPaymentElectricityRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.PaymentElectricityRequest;
import com.jalin.jalinappbackend.module.banking.service.PaymentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("${url.map.api}/v1/banking")
public class PaymentBillController {
    @Autowired
    private PaymentBillService paymentBillService;

    @GetMapping("/payment/electricity/prepaid/{customerId}")
    public ResponseEntity<Object> getElectricityPrepaidOptions(@PathVariable String customerId) {
        PrepaidElectricityDto prepaidElectricityDto = paymentBillService.getElectricityPrepaidOptions(customerId);
        return new ResponseEntity<>(
                new SuccessDetailsResponse(true, "Electricity prepaid options successfully found", prepaidElectricityDto),
                HttpStatus.OK);
    }

    @PostMapping(
            path = "/payment/electricity/prepaid/{customerId}/confirm",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> confirmElectricityPrepaidPayment(
            @PathVariable String customerId,
            @Valid @ModelAttribute ConfirmPaymentElectricityRequest requestBody) {
        ConfirmPaymentDetailsDto confirmPaymentDetailsDto = paymentBillService.confirmPaymentElectricityPrepaid(
                customerId,
                UUID.fromString(requestBody.getPrepaidId()));
        return new ResponseEntity<>(confirmPaymentDetailsDto, HttpStatus.OK);
    }

    @PostMapping("/payment/electricity/postpaid/{customerId}/confirm")
    public ResponseEntity<Object> confirmElectricityPostpaidPayment(@PathVariable String customerId) {
        ConfirmPaymentDetailsDto confirmPaymentDetailsDto = paymentBillService.confirmPaymentElectricityPostpaid(customerId);
        return new ResponseEntity<>(confirmPaymentDetailsDto, HttpStatus.OK);
    }

    @PostMapping(
            path = "/payment/electricity/prepaid/{customerId}/pay",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> payElectricityPrepaid(
            @PathVariable String customerId,
            @Valid @ModelAttribute PaymentElectricityRequest requestBody) {
        TransactionDto transactionDto = paymentBillService.payElectricityPrepaid(customerId, requestBody.getAmount());
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/payment/electricity/postpaid/{customerId}/pay",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> payElectricityPostpaid(
            @PathVariable String customerId,
            @Valid @ModelAttribute PaymentElectricityRequest requestBody) {
        TransactionDto transactionDto = paymentBillService.payElectricityPostpaid(customerId, requestBody.getAmount());
        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }
}
