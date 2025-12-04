package com.libraryapp.service;

import com.libraryapp.model.Book;
import com.libraryapp.model.Loan;
import com.libraryapp.model.User;
import com.libraryapp.repository.BookRepository;
import com.libraryapp.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    // Existing: history for a user
    public List<Loan> getHistoryByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    // Only currently borrowed physical books
    public List<Loan> getCurrentPhysicalLoansByUser(Long userId) {
        return loanRepository.findByUserIdAndLoanTypeAndReturnDateIsNull(userId, "Physical");
    }

    // Log an e-book read
    public void logEbookRead(User user, Book book) {
        if (user == null || book == null) {
            return;
        }
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanType("Ebook_Read");
        loan.setLoanDate(LocalDateTime.now());
        loanRepository.save(loan);
    }

    // Borrow a physical book (marks book as not available)
    public void borrowPhysicalBook(User user, Book book) {
        if (user == null || book == null) {
            return;
        }
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is already borrowed.");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanType("Physical");
        loan.setLoanDate(LocalDateTime.now());
        loanRepository.save(loan);

        // mark book as not available
        book.setAvailable(false);
        bookRepository.save(book);
    }

    // 🔹 Admin: mark physical book as returned using bookId
    public void returnPhysicalBookByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        loanRepository.findFirstByBookIdAndLoanTypeAndReturnDateIsNull(bookId, "Physical")
                .ifPresent(loan -> {
                    loan.setReturnDate(LocalDateTime.now());
                    loanRepository.save(loan);
                });

        // make book available again
        book.setAvailable(true);
        bookRepository.save(book);
    }
}
