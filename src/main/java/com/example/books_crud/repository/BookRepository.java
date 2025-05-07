package com.example.books_crud.repository;

import com.example.books_crud.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing literary work data access
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Custom query to retrieve literary works with their creator information
     */
    @Query("SELECT w.title, w.isbn, c.name FROM Book w JOIN w.creator c")
    List<Object[]> retrieveWorksWithCreators();
    
    /**
     * Find literary works containing the given title substring
     */
    List<Book> findByTitleContainingIgnoreCase(String titleFragment);
    
    /**
     * Find literary works by ISBN code
     */
    Book findByIsbn(String isbnCode);
    
    /**
     * Find all literary works by a specific creator ID
     */
    @Query("SELECT w FROM Book w WHERE w.creator.creatorId = :creatorId")
    List<Book> findAllWorksByCreatorId(@Param("creatorId") Long creatorId);
}