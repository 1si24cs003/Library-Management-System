package com.libraryapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Basic Web MVC settings for direct view mappings
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Maps certain URLs directly to view templates (Thymeleaf),
     * without needing a dedicated controller method.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // Login page handling
        registry.addViewController("/login").setViewName("login");

        // Optional future mapping if needed:
        // registry.addViewController("/catalog").setViewName("catalog");
        // registry.addViewController("/register").setViewName("register");
    }
}
