package com.jalin.jalinappbackend.seeder;

import com.jalin.jalinappbackend.module.authentication.entity.User;
import com.jalin.jalinappbackend.module.authentication.entity.UserDetails;
import com.jalin.jalinappbackend.module.authentication.repository.UserRepository;
import com.jalin.jalinappbackend.module.authentication.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(value = 3)
@Slf4j
public class UserSeeder implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void run(String... args) {
        try {
            seed();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private void seed() {
        if (userRepository.count() == 0) {
            User userTony = new User();
            userTony.setEmail("tonystark@gmail.com");
            userTony.setPassword("password");

            UserDetails userDetailsTony = new UserDetails();
            userDetailsTony.setIdCardNumber("3306124403910309");
            userDetailsTony.setFullName("Tony Stark");
            userDetailsTony.setDateOfBirth(LocalDate.of(1970, 5, 29));
            userDetailsTony.setAddress("Jl. Metro Raya, Pondok Indah, Jakarta Selatan, DKI Jakarta");

            userDetailsTony.setBankingGoals("Savings");
            userDetailsTony.setOccupation("Businessman");
            userDetailsTony.setSourceOfIncome("Revenue/Profit");
            userDetailsTony.setIncomeRange("IDR > 100 million");

            userDetailsTony.setMobileNumber("082111111111");

            authenticationService.register(userTony, userDetailsTony);

            User userBruce = new User();
            userBruce.setEmail("brucewayne@gmail.com");
            userBruce.setPassword("password");

            UserDetails userDetailsBruce = new UserDetails();
            userDetailsBruce.setIdCardNumber("3306124403910329");
            userDetailsBruce.setFullName("Bruce Wayne");
            userDetailsBruce.setDateOfBirth(LocalDate.of(1978, 4, 17));
            userDetailsBruce.setAddress("Jl. Syamsu Rizal, Menteng, Jakarta Pusat, DKI Jakarta");

            userDetailsBruce.setBankingGoals("Savings");
            userDetailsBruce.setOccupation("Businessman");
            userDetailsBruce.setSourceOfIncome("Revenue/Profit");
            userDetailsBruce.setIncomeRange("IDR > 100 million");

            userDetailsBruce.setMobileNumber("082122222222");

            authenticationService.register(userBruce, userDetailsBruce);
        }
    }
}
