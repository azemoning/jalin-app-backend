package com.jalin.jalinappbackend.seeder;

import com.jalin.jalinappbackend.module.authentication.entity.Role;
import com.jalin.jalinappbackend.module.authentication.entity.RoleEnum;
import com.jalin.jalinappbackend.module.authentication.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
@Slf4j
public class RoleSeeder implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        try {
            seed();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

    }

    private void seed() {
        if (roleRepository.count() == 0) {
            Role roleAdmin = new Role();
            roleAdmin.setRoleName(RoleEnum.ADMIN);
            roleRepository.save(roleAdmin);

            Role roleUser = new Role();
            roleUser.setRoleName(RoleEnum.USER);
            roleRepository.save(roleUser);
        }
    }
}
