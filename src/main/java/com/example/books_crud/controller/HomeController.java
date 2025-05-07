package com.example.books_crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Main landing page controller
 */
@Controller
public class HomeController {

    /**
     * Default landing page redirects to catalog view
     */
    @GetMapping("/")
    public String landingPage() {
        return "redirect:/catalog";
    }
    
    /**
     * About page with system information
     */
    @GetMapping("/about")
    public String aboutSystem(Model model) {
        model.addAttribute("applicationName", "Literary Works Management System");
        model.addAttribute("versionInfo", "v1.2.0");
        return "about";
    }
    
    /**
     * Contact page
     */
    @GetMapping("/contact")
    public String contactInfo() {
        return "contact";
    }
}