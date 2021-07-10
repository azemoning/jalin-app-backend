package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.Role;
import com.jalin.jalinappbackend.module.authentication.entity.RoleEnum;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.RoleRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActiveCustomerServiceImpl implements ActiveCustomerService {

    @Autowired
    private UserUtility userUtility;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public int getActiveCustomer() {
        return userUtility.getAllUsers().size();
    }
}
