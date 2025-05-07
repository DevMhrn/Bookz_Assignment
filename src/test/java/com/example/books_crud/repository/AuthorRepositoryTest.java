package com.example.books_crud.repository;

import com.example.books_crud.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository creatorRepository;

    @Test
    @DisplayName("Should find creator by ID")
    void shouldFindCreatorById() {
        // Set up test data
        Author literaryCreator = new Author();
        literaryCreator.setName("Distinguished Writer");
        literaryCreator.setBio("Renowned for experimental fiction");
        entityManager.persist(literaryCreator);
        entityManager.flush();

        // Execute repository method
        Optional<Author> retrievedCreator = creatorRepository.findById(literaryCreator.getCreatorId());

        // Verify results
        assertTrue(retrievedCreator.isPresent(), "Creator should be found");
        assertEquals("Distinguished Writer", retrievedCreator.get().getName(), "Creator name should match");
        assertEquals("Renowned for experimental fiction", retrievedCreator.get().getBio(), "Bio should match");
    }

    @Test
    @DisplayName("Should retrieve all creators")
    void shouldRetrieveAllCreators() {
        // Set up test data
        Author creator1 = new Author();
        creator1.setName("Contemporary Novelist");
        creator1.setBio("Urban fiction specialist");
        entityManager.persist(creator1);

        Author creator2 = new Author();
        creator2.setName("Classic Playwright");
        creator2.setBio("Victorian era dramatist");
        entityManager.persist(creator2);

        entityManager.flush();

        // Execute repository method
        List<Author> creatorsList = creatorRepository.findAll();

        // Verify results
        assertNotNull(creatorsList, "Results should not be null");
        assertEquals(2, creatorsList.size(), "Should find exactly two creators");
    }

    @Test
    @DisplayName("Should persist creator")
    void shouldPersistCreator() {
        // Set up test data
        Author newCreator = new Author();
        newCreator.setName("Emerging Poet");
        newCreator.setBio("Award-winning verse on modern themes");

        // Execute repository method
        Author savedCreator = creatorRepository.save(newCreator);

        // Verify results
        assertNotNull(savedCreator.getCreatorId(), "ID should be generated");
        assertEquals("Emerging Poet", savedCreator.getName(), "Creator name should match");
        assertEquals("Award-winning verse on modern themes", savedCreator.getBio(), "Bio should match");

        // Verify it's in the database
        Author foundCreator = entityManager.find(Author.class, savedCreator.getCreatorId());
        assertNotNull(foundCreator, "Should find creator in database");
        assertEquals("Emerging Poet", foundCreator.getName(), "Creator name should match");
    }

    @Test
    @DisplayName("Should delete creator")
    void shouldDeleteCreator() {
        // Set up test data
        Author disposableCreator = new Author();
        disposableCreator.setName("Temporary Creator");
        disposableCreator.setBio("Created only for deletion test");
        entityManager.persist(disposableCreator);
        entityManager.flush();

        Long idToDelete = disposableCreator.getCreatorId();

        // Execute repository method
        creatorRepository.deleteById(idToDelete);

        // Verify deletion
        Author shouldBeNull = entityManager.find(Author.class, idToDelete);
        assertNull(shouldBeNull, "Creator should be deleted");
    }
    
    @Test
    @DisplayName("Should find creators by name fragment")
    void shouldFindByNameFragment() {
        // Set up test data
        Author creator1 = new Author();
        creator1.setName("Fantasy Writer");
        creator1.setBio("Creates epic fantasy worlds");
        entityManager.persist(creator1);
        
        Author creator2 = new Author();
        creator2.setName("Science Writer");
        creator2.setBio("Writes about scientific discoveries");
        entityManager.persist(creator2);
        
        Author creator3 = new Author();
        creator3.setName("Fantasy Illustrator");
        creator3.setBio("Illustrates fantasy novels");
        entityManager.persist(creator3);
        
        entityManager.flush();
        
        // Execute repository method
        List<Author> foundCreators = creatorRepository.findByNameContainingIgnoreCase("fantasy");
        
        // Verify results
        assertNotNull(foundCreators, "Results should not be null");
        assertEquals(2, foundCreators.size(), "Should find exactly two creators");
        
        // Verify all found creators contain "fantasy" in name
        foundCreators.forEach(creator -> 
            assertTrue(creator.getName().toLowerCase().contains("fantasy"),
                      "Creator name should contain search term")
        );
    }
}