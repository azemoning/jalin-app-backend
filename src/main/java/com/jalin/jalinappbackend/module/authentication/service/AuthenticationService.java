package com.jalin.jalinappbackend.module.authentication.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;

public interface AuthenticationService {
    void register(User userRequestBody, UserDetails userDetailsRequestBody);
    void login(String email, String password);
}
