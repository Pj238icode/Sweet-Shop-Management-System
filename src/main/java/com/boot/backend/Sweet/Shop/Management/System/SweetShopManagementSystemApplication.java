package com.boot.backend.Sweet.Shop.Management.System;

import com.boot.backend.Sweet.Shop.Management.System.entity.Role;
import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import com.boot.backend.Sweet.Shop.Management.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class SweetShopManagementSystemApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SweetShopManagementSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {

        String adminEmail = "admin@sweetshop.com";
        String adminPassword = "admin123";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = User.builder()
                    .name("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);

            System.out.println("ADMIN user created");
        } else {
            System.out.println("ADMIN user already exists");
        }
    }
}
