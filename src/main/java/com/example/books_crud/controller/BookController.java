package com.example.books_crud.controller;

import com.example.books_crud.model.Book;
import com.example.books_crud.service.AuthorService;
import com.example.books_crud.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing literary works in the catalog
 */
@Controller
@RequestMapping("/catalog")
public class BookController {

    private final BookService literaryService;
    private final AuthorService creatorService;

    @Autowired
    public BookController(BookService literaryService, AuthorService creatorService) {
        this.literaryService = literaryService;
        this.creatorService = creatorService;
    }

    /**
     * Display the complete catalog of literary works
     */
    @GetMapping
    public String displayCatalog(Model model) {
        model.addAttribute("literaryWorks", literaryService.browseCatalog());
        return "catalogView";
    }
    
    /**
     * Show literary works with their creators
     */
    @GetMapping("/detailed-view")
    public String detailedCatalogView(Model model) {
        List<Object[]> catalogWithCreators = literaryService.getCatalogWithCreators();
        model.addAttribute("catalogItems", catalogWithCreators);
        return "detailedCatalogView";
    }

    /**
     * Show form for adding a new literary work
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("literaryWork", new Book());
        model.addAttribute("creators", creatorService.findAllCreators());
        return "registerWork";
    }

    /**
     * Process literary work registration
     */
    @PostMapping("/register")
    public String processWorkRegistration(@ModelAttribute("literaryWork") Book work, 
                                         RedirectAttributes notification) {
        try {
            literaryService.registerLiteraryWork(work);
            notification.addFlashAttribute("notification", "Literary work successfully registered");
            return "redirect:/catalog";
        } catch (Exception e) {
            notification.addFlashAttribute("errorMessage", 
                                          "Registration failed: " + e.getMessage());
            return "redirect:/catalog/register";
        }
    }

    /**
     * Show form to edit literary work details
     */
    @GetMapping("/edit/{workId}")
    public String showEditForm(@PathVariable Long workId, Model model, 
                              RedirectAttributes notification) {
        Optional<Book> workToEdit = literaryService.locateLiteraryWork(workId);
        if (workToEdit.isPresent()) {
            model.addAttribute("literaryWork", workToEdit.get());
            model.addAttribute("creators", creatorService.findAllCreators());
            return "editWorkForm";
        } else {
            notification.addFlashAttribute("errorMessage", "Literary work not found in catalog");
            return "redirect:/catalog";
        }
    }

    /**
     * Process literary work updates
     */
    @PostMapping("/edit")
    public String processWorkUpdate(@ModelAttribute("literaryWork") Book updatedWork, 
                                   RedirectAttributes notification) {
        try {
            literaryService.reviseWorkDetails(updatedWork);
            notification.addFlashAttribute("notification", "Literary work details updated");
            return "redirect:/catalog";
        } catch (Exception e) {
            notification.addFlashAttribute("errorMessage", 
                                          "Update failed: " + e.getMessage());
            return "redirect:/catalog/edit/" + updatedWork.getId();
        }
    }
    
    /**
     * Search literary works by title
     */
    @GetMapping("/search")
    public String searchWorks(@RequestParam("query") String searchQuery, Model model) {
        model.addAttribute("literaryWorks", literaryService.searchByTitle(searchQuery));
        model.addAttribute("searchPerformed", true);
        model.addAttribute("searchQuery", searchQuery);
        return "catalogView";
    }
}