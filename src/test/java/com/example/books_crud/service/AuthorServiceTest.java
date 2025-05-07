package com.example.books_crud.service;

import com.example.books_crud.model.Author;
import com.example.books_crud.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository creatorRepository;

    @InjectMocks
    private AuthorService creatorService;

    private Author sampleCreator;

    @BeforeEach
    void prepareTestData() {
        sampleCreator = new Author();
        sampleCreator.setCreatorId(101L);
        sampleCreator.setName("Sample Creator");
        sampleCreator.setBio("Sample biography for testing purposes");
    }

    @Test
    @DisplayName("Should successfully register a new creator")
    void shouldRegisterNewCreator() {
        // Arrange
        when(creatorRepository.save(any(Author.class))).thenReturn(sampleCreator);
        
        // Act
        Author registeredCreator = creatorService.registerCreator(sampleCreator);
        
        // Assert
        assertNotNull(registeredCreator, "Registered creator should not be null");
        assertEquals(101L, registeredCreator.getCreatorId(), "Creator ID should match");
        assertEquals("Sample Creator", registeredCreator.getName(), "Creator name should match");
        assertEquals("Sample biography for testing purposes", registeredCreator.getBio(), "Creator bio should match");
        
        // Verify repository was called
        verify(creatorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("Should retrieve all creators")
    void shouldFindAllCreators() {
        // Arrange
        List<Author> creatorsList = Arrays.asList(sampleCreator);
        when(creatorRepository.findAll()).thenReturn(creatorsList);
        
        // Act
        List<Author> retrievedCreators = creatorService.findAllCreators();
        
        // Assert
        assertNotNull(retrievedCreators, "Returned list should not be null");
        assertEquals(1, retrievedCreators.size(), "Should return exactly one creator");
        assertEquals(101L, retrievedCreators.get(0).getCreatorId(), "Creator ID should match");
        
        // Verify repository was called
        verify(creatorRepository).findAll();
    }

    @Test
    @DisplayName("Should find creator by ID")
    void shouldFindCreatorById() {
        // Arrange
        when(creatorRepository.findById(101L)).thenReturn(Optional.of(sampleCreator));
        
        // Act
        Optional<Author> foundCreator = creatorService.findCreatorById(101L);
        
        // Assert
        assertTrue(foundCreator.isPresent(), "Creator should be found");
        assertEquals(101L, foundCreator.get().getCreatorId(), "Creator ID should match");
        
        // Verify repository was called
        verify(creatorRepository).findById(101L);
    }

    @Test
    @DisplayName("Should update creator details")
    void shouldModifyCreatorDetails() {
        // Arrange
        when(creatorRepository.existsById(101L)).thenReturn(true);
        when(creatorRepository.save(any(Author.class))).thenReturn(sampleCreator);
        
        // Act
        Author updatedCreator = creatorService.modifyCreatorDetails(sampleCreator);
        
        // Assert
        assertNotNull(updatedCreator, "Updated creator should not be null");
        assertEquals(101L, updatedCreator.getCreatorId(), "Creator ID should match");
        
        // Verify repository was called
        verify(creatorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("Should remove creator by ID")
    void shouldRemoveCreator() {
        // Arrange
        doNothing().when(creatorRepository).deleteById(101L);
        
        // Act
        creatorService.removeCreator(101L);
        
        // Verify repository was called
        verify(creatorRepository).deleteById(101L);
    }
    
    @Test
    @DisplayName("Should find creators by name fragment")
    void shouldSearchCreatorsByName() {
        // Arrange
        List<Author> matchingCreators = Arrays.asList(sampleCreator);
        when(creatorRepository.findByNameContainingIgnoreCase("Sample")).thenReturn(matchingCreators);
        
        // Act
        List<Author> foundCreators = creatorService.searchCreatorsByName("Sample");
        
        // Assert
        assertNotNull(foundCreators, "Found creators should not be null");
        assertEquals(1, foundCreators.size(), "Should find exactly one creator");
        assertEquals("Sample Creator", foundCreators.get(0).getName(), "Creator name should match");
        
        // Verify repository was called
        verify(creatorRepository).findByNameContainingIgnoreCase("Sample");
    }
}