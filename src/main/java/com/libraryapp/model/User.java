package com.libraryapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // Avoid reserved SQL keyword "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password; // Hashed password

    @Email(message = "Enter a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Spring Security requires roles in format: ROLE_USER or ROLE_ADMIN
     */
    @Column(nullable = false)
    private String role = "ROLE_USER";  // Default role

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Optional: if later you want user loan history bidirectionally:
    // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    // private List<Loan> loans = new ArrayList<>();


    // JPA Required Constructor
    public User() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ------ Getters & Setters ------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
