package com.libraryapp.controller;

import com.libraryapp.model.Book;
import com.libraryapp.model.User;
import com.libraryapp.service.BookService;
import com.libraryapp.service.LoanService;
import com.libraryapp.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.security.Principal;

@Controller
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;
    private final UserRepository userRepository;

    public BookController(BookService bookService,
                          LoanService loanService,
                          UserRepository userRepository) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.userRepository = userRepository;
    }

    // Shows the catalog of books at /catalog
    @GetMapping("/catalog")
    public String viewHomePage(Model model) {
        model.addAttribute("listBooks", bookService.findAllBooks());
        return "catalog"; // catalog.html
    }

    // --- NEW: Borrow a physical book ---
    @GetMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Book book = bookService.findBookById(id);

        // Only meaningful for physical books; but even if ebook, it's harmless
        loanService.borrowPhysicalBook(user, book);

        return "redirect:/mybooks";
    }

    // Admin: show form
    @GetMapping("/admin/showNewBookForm")
    public String showNewBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add_book";
    }

    // Admin: save book
    @PostMapping("/admin/saveBook")
    public String saveBook(@ModelAttribute("book") Book book,
                           @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            bookService.saveBook(book, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/catalog";
    }

    // E-book streaming
    @GetMapping("/read/{fileName}")
    public ResponseEntity<Resource> readEbook(@PathVariable String fileName) {
        try {
            Path filePath = bookService.loadFileAsResource(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = "application/pdf";
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
