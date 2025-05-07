package com.example.books_crud.repository;

import com.example.books_crud.model.Author;
import com.example.books_crud.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository literaryWorkRepo;

    @Test
    @DisplayName("Should retrieve works with their creators")
    void shouldRetrieveWorksWithCreators() {
        // Set up test data
        Author literaryCreator = new Author();
        literaryCreator.setName("Literary Genius");
        literaryCreator.setBio("Award-winning author of multiple genres");
        entityManager.persist(literaryCreator);

        Book literaryWork = new Book();
        literaryWork.setTitle("Masterpiece Novel");
        literaryWork.setIsbn("ABC9876543210");
        literaryWork.setCreator(literaryCreator);
        entityManager.persist(literaryWork);

        entityManager.flush();

        // Execute the repository method
        List<Object[]> queryResults = literaryWorkRepo.retrieveWorksWithCreators();

        // Verify results
        assertNotNull(queryResults, "Results should not be null");
        assertFalse(queryResults.isEmpty(), "Results should not be empty");
        assertEquals(1, queryResults.size(), "Should have exactly one result");
        
        Object[] firstResult = queryResults.get(0);
        assertEquals("Masterpiece Novel", firstResult[0], "Work title should match");
        assertEquals("ABC9876543210", firstResult[1], "ISBN should match");
        assertEquals("Literary Genius", firstResult[2], "Creator name should match");
    }
    
    @Test
    @DisplayName("Should find works by title fragment")
    void shouldFindByTitleFragment() {
        // Set up test data
        Author creator = new Author();
        creator.setName("Test Creator");
        entityManager.persist(creator);
        
        Book work1 = new Book();
        work1.setTitle("Fantasy Adventure");
        work1.setIsbn("123456");
        work1.setCreator(creator);
        entityManager.persist(work1);
        
        Book work2 = new Book();
        work2.setTitle("Historical Fantasy");
        work2.setIsbn("654321");
        work2.setCreator(creator);
        entityManager.persist(work2);
        
        Book work3 = new Book();
        work3.setTitle("Science Fiction");
        work3.setIsbn("789012");
        work3.setCreator(creator);
        entityManager.persist(work3);
        
        entityManager.flush();
        
        // Execute repository method
        List<Book> foundWorks = literaryWorkRepo.findByTitleContainingIgnoreCase("fantasy");
        
        // Verify results
        assertNotNull(foundWorks, "Results should not be null");
        assertEquals(2, foundWorks.size(), "Should find exactly two works");
        
        // Verify all found works contain "fantasy" in title
        foundWorks.forEach(work -> 
            assertTrue(work.getTitle().toLowerCase().contains("fantasy"),
                      "Work title should contain search term")
        );
    }
}