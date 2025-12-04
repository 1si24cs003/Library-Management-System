package com.libraryapp.library_manager;

import com.libraryapp.model.User;
import com.libraryapp.repository.UserRepository;
import com.libraryapp.util.FileStorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
@EnableJpaRepositories(basePackages = "com.libraryapp.repository")
@EntityScan(basePackages = "com.libraryapp.model")
public class LibraryManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagerApplication.class, args);
    }

    /**
     * This runs once at startup and ensures there are two users in the DB:
     *
     * USER:
     *   username: testuser
     *   password: test123
     *   role    : USER
     *
     * ADMIN:
     *   username: admin
     *   password: admin123
     *   role    : ADMIN
     */
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {

            // Create normal user if not exists
            userRepository.findByUsername("testuser")
                    .orElseGet(() -> {
                        User u = new User();
                        u.setUsername("testuser");
                        u.setEmail("testuser@example.com");
                        u.setPassword(passwordEncoder.encode("test123"));
                        u.setRole("USER");  // IMPORTANT: no "ROLE_" prefix here
                        System.out.println(">> Creating default USER: testuser / test123");
                        return userRepository.save(u);
                    });

            // Create admin user if not exists
            userRepository.findByUsername("admin")
                    .orElseGet(() -> {
                        User a = new User();
                        a.setUsername("admin");
                        a.setEmail("admin@example.com");
                        a.setPassword(passwordEncoder.encode("admin123"));
                        a.setRole("ADMIN"); // will become ROLE_ADMIN in Security
                        System.out.println(">> Creating default ADMIN: admin / admin123");
                        return userRepository.save(a);
                    });
        };
    }
} 
