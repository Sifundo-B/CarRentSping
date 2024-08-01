package com.Rental.rental.config;

import com.Rental.rental.entity.User;
import com.Rental.rental.enums.Role;
import com.Rental.rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Runs the command line task to initialize a default admin user.
     * @param args command line arguments
     * @throws Exception if an error occurs during execution
     */
    @Override
    public void run(String... args) throws Exception {
        // Check if an admin user already exists
        if (userRepo.findByEmail("admin@example.com").isEmpty()) {
            // Create a default admin user
            User admin = User.builder()
                    .firstName("Default")
                    .lastName("Admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .address("123 Admin Street")
                    .phoneNumber("1234567890")
                    .role(Role.ADMIN)
                    .build();

            // Save the admin user to the repository
            userRepo.save(admin);
        }
    }
}
