package com.libraryapp.controller;

import com.libraryapp.model.User;
import com.libraryapp.repository.UserRepository;
import com.libraryapp.service.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    private final LoanService loanService;
    private final UserRepository userRepository;

    public UserController(LoanService loanService,
                          UserRepository userRepository) {
        this.loanService = loanService;
        this.userRepository = userRepository;
    }

    // Full history page: /history -> history.html
    @GetMapping("/history")
    public String viewHistory(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("loanHistory", loanService.getHistoryByUser(user.getId()));
        return "history"; // templates/history.html
    }

    // Current borrowed physical books page: /mybooks -> mybooks.html
    @GetMapping("/mybooks")
    public String viewMyBorrowedBooks(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("loans", loanService.getCurrentPhysicalLoansByUser(user.getId()));
        return "mybooks"; // templates/mybooks.html
    }
}
