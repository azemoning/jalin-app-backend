package com.jalin.jalinappbackend.module.authentication.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;

public interface AuthenticationService {
    void register(String mobileNumberRequestBody, User userRequestBody);
    void login(String email, String password);
}
