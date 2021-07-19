package com.jalin.jalinappbackend.module.banking.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessDetailsResponse;
import com.jalin.jalinappbackend.module.banking.model.PrepaidElectricityDto;
import com.jalin.jalinappbackend.module.banking.service.PaymentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
