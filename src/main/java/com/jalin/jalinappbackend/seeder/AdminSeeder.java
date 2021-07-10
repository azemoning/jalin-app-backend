package com.jalin.jalinappbackend.seeder;

import com.jalin.jalinappbackend.exception.ResourceNotFoundException;
import com.jalin.jalinappbackend.module.authentication.entity.Role;
import com.jalin.jalinappbackend.module.authentication.entity.RoleEnum;
import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.repository.RoleRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserDetailsRepository;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(value = 5)
@Slf4j
public class AdminSeeder implements CommandLineRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args){
        try {
            seed();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private void seed() {
        Role adminRole = roleRepository.findByRoleName(RoleEnum.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        if (userRepository.findByRole(adminRole).isEmpty()) {
            User userAdmin = new User();
            userAdmin.setEmail("admin@jalin.id");
            userAdmin.setPassword(passwordEncoder.encode("password"));
            userAdmin.setRole(adminRole);
            userRepository.save(userAdmin);
        }
    }
}
