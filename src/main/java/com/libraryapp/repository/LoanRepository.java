package com.libraryapp.repository;

import com.libraryapp.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Full history
    List<Loan> findByUserId(Long userId);

    // Only current physical loans (not returned yet)
    List<Loan> findByUserIdAndLoanTypeAndReturnDateIsNull(Long userId, String loanType);

    // 🔹 Find active physical loan for a given book
    Optional<Loan> findFirstByBookIdAndLoanTypeAndReturnDateIsNull(Long bookId, String loanType);
}
