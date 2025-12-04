package com.libraryapp.repository;

import com.libraryapp.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserId(Long userId);

    List<Loan> findByUserIdAndLoanTypeAndReturnDateIsNull(Long userId, String loanType);

    // 🆕 Easier user-based lookup
    List<Loan> findByUserUsername(String username);
}
