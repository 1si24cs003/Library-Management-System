package com.libraryapp.service;

import com.libraryapp.model.Book;
import com.libraryapp.repository.BookRepository;
import com.libraryapp.util.FileStorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final Path fileStorageLocation;

    public BookService(BookRepository bookRepository,
                       FileStorageProperties fileStorageProperties) throws IOException {
        this.bookRepository = bookRepository;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    // NEW: helper for borrow endpoint
    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public Book saveBook(Book book, MultipartFile file) throws IOException {
        if (book.isEbook() && file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            book.setFilePath(fileName);
        } else if (!book.isEbook()) {
            book.setFilePath(null);
        }
        return bookRepository.save(book);
    }

    public Path loadFileAsResource(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }
}
