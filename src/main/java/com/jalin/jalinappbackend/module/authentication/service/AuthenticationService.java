package com.jalin.jalinappbackend.module.authentication.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.model.LoginAdminDto;
import com.jalin.jalinappbackend.module.authentication.model.LoginDto;

public interface AuthenticationService {
    void register(User userRequestBody, UserDetails userDetailsRequestBody);
    LoginDto login(String email, String password);
    LoginAdminDto loginAdmin(String email, String password);
}
