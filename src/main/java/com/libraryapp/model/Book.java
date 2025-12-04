package com.libraryapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "books") // Explicit table name
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author name is required")
    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbn;

    @Column(name = "is_ebook", nullable = false)
    private boolean ebook;  // Avoids "is" prefix confusion with getter/setter

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // JPA requires a no-args constructor
    public Book() {}

    // Optional convenience constructor
    public Book(String title, String author, String isbn, boolean ebook, String filePath) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.ebook = ebook;
        this.filePath = filePath;
        this.createdAt = LocalDateTime.now();
    }

    // Automatically set created timestamp before persisting
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isEbook() {
        return ebook;
    }

    public void setEbook(boolean ebook) {
        this.ebook = ebook;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
