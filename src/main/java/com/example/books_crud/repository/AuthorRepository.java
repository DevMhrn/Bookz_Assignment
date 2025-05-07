package com.example.books_crud.repository;

import com.example.books_crud.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing literary creator data access
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    /**
     * Find literary creators by name containing the specified text
     */
    List<Author> findByNameContainingIgnoreCase(String nameFragment);
    
    /**
     * Find literary creators by biography containing the specified text
     */
    List<Author> findByBioContainingIgnoreCase(String bioFragment);
    
    /**
     * Count literary creators with works greater than the specified count
     */
    @Query("SELECT COUNT(a) FROM Author a WHERE SIZE(a.literaryWorks) > ?1")
    long countCreatorsWithMultipleWorks(int workCount);
}