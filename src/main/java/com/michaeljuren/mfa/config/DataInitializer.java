package com.michaeljuren.mfa.config;

import com.michaeljuren.mfa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Create a test user
        // Username: user
        // Password: password
        userService.createUser("user", "password");

        System.out.println("====================================");
        System.out.println("Test user created!");
        System.out.println("Username: user");
        System.out.println("Password: password");
        System.out.println("====================================");
    }
}
