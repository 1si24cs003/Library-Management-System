package com.libraryapp.library_manager;

import com.libraryapp.model.User;
import com.libraryapp.model.Book;
import com.libraryapp.repository.BookRepository;
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

@SpringBootApplication(scanBasePackages = "com.libraryapp")   // 🔹 important!
@EnableConfigurationProperties({FileStorageProperties.class})
@EnableJpaRepositories(basePackages = "com.libraryapp.repository")
@EntityScan(basePackages = "com.libraryapp.model")
public class LibraryManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagerApplication.class, args);
    }

    // Create default users
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("testuser").isEmpty()) {
                User u = new User();
                u.setUsername("testuser");
                u.setEmail("testuser@example.com");
                u.setPassword(passwordEncoder.encode("test123"));
                u.setRole("USER");
                u.setCreatedAt(LocalDateTime.now());
                userRepository.save(u);
                System.out.println(">> Added USER: testuser / test123");
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                User a = new User();
                a.setUsername("admin");
                a.setEmail("admin@example.com");
                a.setPassword(passwordEncoder.encode("admin123"));
                a.setRole("ADMIN");
                a.setCreatedAt(LocalDateTime.now());
                userRepository.save(a);
                System.out.println(">> Added ADMIN: admin / admin123");
            }
        };
    }

    // Insert sample books only if no books exist
    @Bean
    public CommandLineRunner initBooks(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() == 0) {
                System.out.println(">> Adding sample books...");

                bookRepository.save(new Book(
                        "The Great Gatsby", "F. Scott Fitzgerald",
                        "9780743273565", false, null
                ));
                bookRepository.save(new Book(
                        "1984", "George Orwell",
                        "9780451524935", false, null
                ));
                bookRepository.save(new Book(
                        "Atomic Habits", "James Clear",
                        "9780735211292", false, null
                ));
                bookRepository.save(new Book(
                        "Clean Code", "Robert C. Martin",
                        "9780132350884", true, null
                ));
                bookRepository.save(new Book(
                        "Java: The Complete Reference",
                        "Herbert Schildt",
                        "9781260440232", true, null
                ));
            }
        };
    }
}
