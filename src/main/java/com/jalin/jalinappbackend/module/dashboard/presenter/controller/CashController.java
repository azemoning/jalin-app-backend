package com.jalin.jalinappbackend.module.dashboard.presenter.controller;

import com.jalin.jalinappbackend.module.dashboard.model.CashDto;
import com.jalin.jalinappbackend.module.dashboard.service.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.map.api}/admin/v1")
public class CashController {
    @Autowired
    private CashService cashService;

    @GetMapping("/cash")
    public ResponseEntity<Object> getCash() {
        CashDto cashDto = cashService.getCash();
        return new ResponseEntity<>(cashDto, HttpStatus.OK);
    }
}
