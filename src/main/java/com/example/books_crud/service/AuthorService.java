package com.example.books_crud.service;

import com.example.books_crud.model.Author;
import com.example.books_crud.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing literary creators
 */
@Service
public class AuthorService {

    private final AuthorRepository creatorRepository;

    @Autowired
    public AuthorService(AuthorRepository creatorRepository) {
        this.creatorRepository = creatorRepository;
    }

    /**
     * Register a new literary creator in the system
     */
    @Transactional
    public Author registerCreator(Author creator) {
        // Validate creator data before saving
        if (creator.getName() == null || creator.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Creator name cannot be empty");
        }
        return creatorRepository.save(creator);
    }

    /**
     * Retrieve all literary creators
     */
    public List<Author> findAllCreators() {
        return creatorRepository.findAll();
    }

    /**
     * Locate a creator by their unique identifier
     */
    public Optional<Author> findCreatorById(Long creatorId) {
        return creatorRepository.findById(creatorId);
    }

    /**
     * Modify an existing creator's information
     */
    @Transactional
    public Author modifyCreatorDetails(Author updatedCreator) {
        // Ensure the creator exists before updating
        if (updatedCreator.getCreatorId() == null || 
            !creatorRepository.existsById(updatedCreator.getCreatorId())) {
            throw new IllegalArgumentException("Cannot update non-existent creator");
        }
        return creatorRepository.save(updatedCreator);
    }

    /**
     * Remove a creator from the system
     */
    @Transactional
    public void removeCreator(Long creatorId) {
        creatorRepository.deleteById(creatorId);
    }
    
    /**
     * Find creators by name fragment
     */
    public List<Author> searchCreatorsByName(String nameFragment) {
        return creatorRepository.findByNameContainingIgnoreCase(nameFragment);
    }
}