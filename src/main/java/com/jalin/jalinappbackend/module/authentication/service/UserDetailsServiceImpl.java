package com.jalin.jalinappbackend.module.authentication.service;

import com.jalin.jalinappbackend.exception.AuthenticateFailedException;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetailsImpl;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticateFailedException("Incorrect email or password"));
        return new UserDetailsImpl(user);
    }
}
