package com.libraryapp.repository;

import com.libraryapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find books containing title text (case insensitive)
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Search books by author name (case insensitive)
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * List only ebooks or only physical books
     */
    List<Book> findByEbook(boolean isEbook);

    /**
     * Optional check for duplicate ISBN
     */
    Optional<Book> findByIsbn(String isbn);
}
