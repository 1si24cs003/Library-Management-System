package com.libraryapp.repository;

import com.libraryapp.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Full history (you already had something like this)
    List<Loan> findByUserId(Long userId);

    // Only current physical loans (not returned yet)
    List<Loan> findByUserIdAndLoanTypeAndReturnDateIsNull(Long userId, String loanType);
}
