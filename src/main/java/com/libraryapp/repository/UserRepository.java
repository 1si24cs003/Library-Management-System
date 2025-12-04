package com.libraryapp.repository;

import com.libraryapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    // 🔹 Add this line:
    boolean existsByUsername(String username);
}
