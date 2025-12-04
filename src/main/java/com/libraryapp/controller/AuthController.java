package com.libraryapp.controller;

import com.libraryapp.model.User;
import com.libraryapp.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Root path -> Home should be protected, so redirect to login
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/catalog";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {

        if (error != null) {
            model.addAttribute("errorMsg", "Invalid username or password!");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.registerNewUser(user);
            return "redirect:/login?registered"; // Show success message on login page
        } catch (DataIntegrityViolationException ex) {
            // Errors like duplicate username/email
            model.addAttribute("errorMsg", "Username or email already exists!");
            return "register";
        }
    }
}
