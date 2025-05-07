package com.example.books_crud.controller;

import com.example.books_crud.model.Author;
import com.example.books_crud.service.AuthorService;
import com.example.books_crud.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller for managing literary creators
 */
@Controller
@RequestMapping("/creators")
public class AuthorController {

    private final AuthorService creatorService;
    private final BookService literaryService;

    @Autowired
    public AuthorController(AuthorService creatorService, BookService literaryService) {
        this.creatorService = creatorService;
        this.literaryService = literaryService;
    }

    /**
     * Display the directory of literary creators
     */
    @GetMapping
    public String displayCreatorDirectory(Model model) {
        model.addAttribute("creatorDirectory", creatorService.findAllCreators());
        return "creatorDirectory";
    }

    /**
     * Show form for registering a new creator
     */
    @GetMapping("/register")
    public String showCreatorRegistrationForm(Model model) {
        model.addAttribute("literaryCreator", new Author());
        return "registerCreator";
    }

    /**
     * Process creator registration
     */
    @PostMapping("/register")
    public String processCreatorRegistration(@ModelAttribute("literaryCreator") Author creator,
                                           RedirectAttributes notification) {
        try {
            creatorService.registerCreator(creator);
            notification.addFlashAttribute("notification", "Literary creator registered successfully");
            return "redirect:/creators";
        } catch (Exception e) {
            notification.addFlashAttribute("errorMessage",
                                          "Registration failed: " + e.getMessage());
            return "redirect:/creators/register";
        }
    }

    /**
     * Show form to edit creator profile
     */
    @GetMapping("/edit/{creatorId}")
    public String showCreatorEditForm(@PathVariable Long creatorId, Model model,
                                     RedirectAttributes notification) {
        Optional<Author> creatorToEdit = creatorService.findCreatorById(creatorId);
        if (creatorToEdit.isPresent()) {
            model.addAttribute("literaryCreator", creatorToEdit.get());
            return "editCreatorProfile";
        } else {
            notification.addFlashAttribute("errorMessage", "Creator not found in directory");
            return "redirect:/creators";
        }
    }

    /**
     * Process creator profile updates
     */
    @PostMapping("/edit")
    public String processProfileUpdate(@ModelAttribute("literaryCreator") Author updatedCreator,
                                      RedirectAttributes notification) {
        try {
            creatorService.modifyCreatorDetails(updatedCreator);
            notification.addFlashAttribute("notification", "Creator profile updated");
            return "redirect:/creators";
        } catch (Exception e) {
            notification.addFlashAttribute("errorMessage",
                                          "Update failed: " + e.getMessage());
            return "redirect:/creators/edit/" + updatedCreator.getCreatorId();
        }
    }

    /**
     * View a creator's published works
     */
    @GetMapping("/{creatorId}/works")
    public String viewCreatorWorks(@PathVariable Long creatorId, Model model,
                                  RedirectAttributes notification) {
        Optional<Author> creator = creatorService.findCreatorById(creatorId);
        if (creator.isPresent()) {
            model.addAttribute("creator", creator.get());
            model.addAttribute("publishedWorks", literaryService.findWorksByCreator(creatorId));
            return "creatorWorks";
        } else {
            notification.addFlashAttribute("errorMessage", "Creator not found");
            return "redirect:/creators";
        }
    }

    /**
     * Search creators by name
     */
    @GetMapping("/search")
    public String searchCreators(@RequestParam("query") String searchQuery, Model model) {
        model.addAttribute("creatorDirectory", creatorService.searchCreatorsByName(searchQuery));
        model.addAttribute("searchPerformed", true);
        model.addAttribute("searchQuery", searchQuery);
        return "creatorDirectory";
    }
}