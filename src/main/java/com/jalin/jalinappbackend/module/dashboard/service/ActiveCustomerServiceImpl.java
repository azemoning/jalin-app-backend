package com.jalin.jalinappbackend.module.dashboard.service;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActiveCustomerServiceImpl implements ActiveCustomerService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public int getActiveCustomer() {
        List<User> users = userRepository.findAll();
        return users.size();
    }
}
