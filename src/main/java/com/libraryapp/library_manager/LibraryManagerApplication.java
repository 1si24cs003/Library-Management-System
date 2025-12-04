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
import java.time.LocalDateTime;

@SpringBootApplication(scanBasePackages = "com.libraryapp")  // 🔹 scan ALL app packages
@EnableConfigurationProperties({FileStorageProperties.class})
@EnableJpaRepositories(basePackages = "com.libraryapp.repository")
@EntityScan(basePackages = "com.libraryapp.model")
public class LibraryManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByUsername("testuser").isEmpty()) {
                User u = new User();
                u.setUsername("testuser");
                u.setEmail("testuser@example.com");
                u.setPassword(passwordEncoder.encode("test123"));
                u.setRole("USER"); // will become ROLE_USER by UserService
                u.setCreatedAt(LocalDateTime.now());
                System.out.println(">> Creating USER: testuser / test123");
                userRepository.save(u);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                User a = new User();
                a.setUsername("admin");
                a.setEmail("admin@example.com");
                a.setPassword(passwordEncoder.encode("admin123"));
                a.setRole("ADMIN"); // will become ROLE_ADMIN by UserService
                a.setCreatedAt(LocalDateTime.now());
                System.out.println(">> Creating ADMIN: admin / admin123");
                userRepository.save(a);
            }
        };
    }
}
