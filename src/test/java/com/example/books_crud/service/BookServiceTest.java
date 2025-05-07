package com.example.books_crud.service;

import com.example.books_crud.model.Author;
import com.example.books_crud.model.Book;
import com.example.books_crud.repository.BookRepository;
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
class BookServiceTest {

    @Mock
    private BookRepository workRepository;

    @InjectMocks
    private BookService literaryService;

    private Book sampleWork;
    private Author sampleCreator;
    private List<Object[]> worksWithCreators;

    @BeforeEach
    void prepareTestData() {
        // Create test author
        sampleCreator = new Author();
        sampleCreator.setCreatorId(101L);
        sampleCreator.setName("Sample Creator");
        sampleCreator.setBio("Sample biography text");

        // Create test book
        sampleWork = new Book();
        sampleWork.setId(201L);
        sampleWork.setTitle("Sample Literary Work");
        sampleWork.setIsbn("1234567890XYZ");
        sampleWork.setCreator(sampleCreator);

        // Sample data for joined query
        worksWithCreators = Arrays.asList(
            new Object[]{"Sample Literary Work", "1234567890XYZ", "Sample Creator"}
        );
    }

    @Test
    @DisplayName("Should successfully register a new literary work")
    void shouldRegisterLiteraryWork() {
        // Arrange
        when(workRepository.save(any(Book.class))).thenReturn(sampleWork);

        // Act
        Book registeredWork = literaryService.registerLiteraryWork(sampleWork);

        // Assert
        assertNotNull(registeredWork, "Registered work should not be null");
        assertEquals(201L, registeredWork.getId(), "ID should match");
        assertEquals("Sample Literary Work", registeredWork.getTitle(), "Title should match");
        assertEquals("1234567890XYZ", registeredWork.getIsbn(), "ISBN should match");
        assertEquals(101L, registeredWork.getCreator().getCreatorId(), "Creator ID should match");

        // Verify repository was called
        verify(workRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should retrieve all works from catalog")
    void shouldBrowseCatalog() {
        // Arrange
        when(workRepository.findAll()).thenReturn(Arrays.asList(sampleWork));

        // Act
        List<Book> catalogWorks = literaryService.browseCatalog();

        // Assert
        assertNotNull(catalogWorks, "Returned list should not be null");
        assertFalse(catalogWorks.isEmpty(), "Catalog should not be empty");
        assertEquals(1, catalogWorks.size(), "Should return exactly one work");
        assertEquals(201L, catalogWorks.get(0).getId(), "Work ID should match");

        // Verify repository was called
        verify(workRepository).findAll();
    }

    @Test
    @DisplayName("Should locate work by ID")
    void shouldLocateWorkById() {
        // Arrange
        when(workRepository.findById(201L)).thenReturn(Optional.of(sampleWork));

        // Act
        Optional<Book> foundWork = literaryService.locateLiteraryWork(201L);

        // Assert
        assertTrue(foundWork.isPresent(), "Work should be found");
        assertEquals(201L, foundWork.get().getId(), "Work ID should match");

        // Verify repository was called
        verify(workRepository).findById(201L);
    }

    @Test
    @DisplayName("Should update work details")
    void shouldReviseWorkDetails() {
        // Arrange
        when(workRepository.existsById(201L)).thenReturn(true);
        when(workRepository.save(any(Book.class))).thenReturn(sampleWork);

        // Act
        Book updatedWork = literaryService.reviseWorkDetails(sampleWork);

        // Assert
        assertNotNull(updatedWork, "Updated work should not be null");
        assertEquals(201L, updatedWork.getId(), "Work ID should match");

        // Verify repository was called
        verify(workRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should remove work from catalog")
    void shouldWithdrawFromCatalog() {
        // Arrange
        doNothing().when(workRepository).deleteById(201L);

        // Act
        literaryService.withdrawFromCatalog(201L);

        // Verify repository was called
        verify(workRepository).deleteById(201L);
    }

    @Test
    @DisplayName("Should retrieve works with their creators")
    void shouldGetCatalogWithCreators() {
        // Arrange
        when(workRepository.retrieveWorksWithCreators()).thenReturn(worksWithCreators);

        // Act
        List<Object[]> results = literaryService.getCatalogWithCreators();

        // Assert
        assertNotNull(results, "Results should not be null");
        assertEquals(1, results.size(), "Should have one result");
        assertEquals("Sample Literary Work", results.get(0)[0], "Title should match");
        assertEquals("1234567890XYZ", results.get(0)[1], "ISBN should match");
        assertEquals("Sample Creator", results.get(0)[2], "Creator name should match");

        // Verify repository was called
        verify(workRepository).retrieveWorksWithCreators();
    }
}
