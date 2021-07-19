package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.module.banking.model.ConfirmPaymentDetailsDto;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;
import com.jalin.jalinappbackend.module.banking.presenter.model.ConfirmPaymentElectricityRequest;
import com.jalin.jalinappbackend.module.banking.presenter.model.ConfirmPaymentMobilePhoneRequest;
import com.jalin.jalinappbackend.module.banking.service.PaymentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/banking")
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

    @PostMapping("payment/electricity/prepaid/{customerId}/confirm")
    public ResponseEntity<Object> confirmElectricityPrepaidPayment(
            @PathVariable String customerId,
            @Valid @ModelAttribute ConfirmPaymentElectricityRequest requestBody) {
        ConfirmPaymentDetailsDto confirmPaymentDetailsDto = paymentBillService.confirmPaymentElectricityPrepaid(
                customerId,
                UUID.fromString(requestBody.getPrepaidId()));
        return new ResponseEntity<>(confirmPaymentDetailsDto, HttpStatus.OK);
    }
}
