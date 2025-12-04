package com.libraryapp.service;

import com.libraryapp.model.Book;
import com.libraryapp.model.Loan;
import com.libraryapp.model.User;
import com.libraryapp.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    // Existing: history for a user
    public List<Loan> getHistoryByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    // NEW: only currently borrowed physical books
    public List<Loan> getCurrentPhysicalLoansByUser(Long userId) {
        return loanRepository.findByUserIdAndLoanTypeAndReturnDateIsNull(userId, "Physical");
    }

    // Existing: log an e-book read (if you already had this, keep it)
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

    // NEW: borrow a physical book
    public void borrowPhysicalBook(User user, Book book) {
        if (user == null || book == null) {
            return;
        }
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanType("Physical");
        loan.setLoanDate(LocalDateTime.now());
        // returnDate stays null while it's borrowed
        loanRepository.save(loan);
    }
}
