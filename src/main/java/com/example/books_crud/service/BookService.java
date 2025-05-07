package com.example.books_crud.service;

import com.example.books_crud.model.Book;
import com.example.books_crud.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing literary works
 */
@Service
public class BookService {

    private final BookRepository literaryWorkRepository;

    @Autowired
    public BookService(BookRepository literaryWorkRepository) {
        this.literaryWorkRepository = literaryWorkRepository;
    }

    /**
     * Register a new literary work in the catalog
     */
    @Transactional
    public Book registerLiteraryWork(Book literaryWork) {
        // Validate work before saving
        if (literaryWork.getTitle() == null || literaryWork.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Literary work must have a title");
        }
        if (literaryWork.getCreator() == null) {
            throw new IllegalArgumentException("Literary work must have a creator");
        }
        return literaryWorkRepository.save(literaryWork);
    }

    /**
     * Retrieve all literary works in the catalog
     */
    public List<Book> browseCatalog() {
        return literaryWorkRepository.findAll();
    }

    /**
     * Find a specific literary work by its identifier
     */
    public Optional<Book> locateLiteraryWork(Long workId) {
        return literaryWorkRepository.findById(workId);
    }

    /**
     * Update a literary work's information
     */
    @Transactional
    public Book reviseWorkDetails(Book revisedWork) {
        // Ensure work exists before updating
        if (revisedWork.getId() == null || 
            !literaryWorkRepository.existsById(revisedWork.getId())) {
            throw new IllegalArgumentException("Cannot update non-existent literary work");
        }
        return literaryWorkRepository.save(revisedWork);
    }

    /**
     * Remove a literary work from the catalog
     */
    @Transactional
    public void withdrawFromCatalog(Long workId) {
        literaryWorkRepository.deleteById(workId);
    }
    
    /**
     * Retrieve literary works with their creator information
     */
    public List<Object[]> getCatalogWithCreators() {
        return literaryWorkRepository.retrieveWorksWithCreators();
    }
    
    /**
     * Find works by a specific creator
     */
    public List<Book> findWorksByCreator(Long creatorId) {
        return literaryWorkRepository.findAllWorksByCreatorId(creatorId);
    }
    
    /**
     * Search works by title keywords
     */
    public List<Book> searchByTitle(String titleFragment) {
        return literaryWorkRepository.findByTitleContainingIgnoreCase(titleFragment);
    }
}