package com.libraryapp.controller;

import com.libraryapp.model.Book;
import com.libraryapp.model.User;
import com.libraryapp.service.BookService;
import com.libraryapp.service.LoanService;
import com.libraryapp.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.security.Principal;

@Controller
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;
    private final UserService userService;

    public BookController(BookService bookService,
                          LoanService loanService,
                          UserService userService) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.userService = userService;
    }

    // Redirect root to catalog
    @GetMapping("/")
    public String home() {
        return "redirect:/catalog";
    }

    // Main library catalog page
    @GetMapping("/catalog")
    public String viewCatalog(Model model) {
        model.addAttribute("listBooks", bookService.findAllBooks());
        return "catalog"; // templates/catalog.html
    }

    // Admin: show "Add New Book" form
    @GetMapping("/admin/showNewBookForm")
    public String showNewBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add_book"; // templates/add_book.html
    }

    // Admin: handle form submit for new book
    @PostMapping("/admin/saveBook")
    public String saveBook(@ModelAttribute("book") Book book,
                           @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        bookService.saveBook(book, file);
        return "redirect:/catalog";
    }

    // Borrow a book (physical) or log an ebook read
    @GetMapping("/borrow/{id}")
    public String borrowBook(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        Book book = bookService.findBookById(id);

        if (book.isEbook()) {
            // For e-books: just log that user read it, then go back to catalog
            loanService.logEbookRead(user, book);
            return "redirect:/catalog";
        } else {
            // For physical books: create a loan and go to "My Borrowed Books"
            loanService.borrowPhysicalBook(user, book);
            return "redirect:/mybooks";
        }
    }

    // 🔹 Admin: mark physical book as returned (make available again)
    @GetMapping("/admin/return/{id}")
    public String returnBook(@PathVariable("id") Long id) {
        loanService.returnPhysicalBookByBookId(id);
        return "redirect:/catalog";
    }

    // Read / view an ebook file in browser
    @GetMapping("/read/{fileName}")
    public ResponseEntity<Resource> readEbook(@PathVariable("fileName") String fileName)
            throws MalformedURLException {

        Path filePath = bookService.loadFileAsResource(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Assume PDF by default, otherwise generic download
        MediaType mediaType = MediaType.APPLICATION_PDF;
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
