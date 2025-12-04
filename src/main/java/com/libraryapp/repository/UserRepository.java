package com.libraryapp.repository;

import com.libraryapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Used by Spring Security during login
     */
    Optional<User> findByUsername(String username);

    /**
     * Check for duplicate email during registration
     */
    Optional<User> findByEmail(String email);

    /**
     * Fetch all users who have a specific role (e.g. ROLE_ADMIN)
     */
    Optional<User> findByRole(String role);

    /**
     * Quick existence checks for validation UI
     */
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
