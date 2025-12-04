package com.libraryapp.controller;

import com.libraryapp.model.User;
import com.libraryapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // -> templates/login.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // -> templates/register.html
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute("user") User user) {
        // Basic: try register, if fails it will show an error page (OK for now)
        userService.registerNewUser(user);
        return "redirect:/register?success";
    }
}
