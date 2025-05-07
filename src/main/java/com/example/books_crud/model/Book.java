package com.example.books_crud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity representing a literary work in the system
 */
@Entity
@Table(name = "literary_work")
@Getter
@Setter
@ToString(exclude = "creator")
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;

    @Column(name = "work_title", nullable = false)
    private String title;

    @Column(name = "international_code", unique = true)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private Author creator;

    /**
     * Convenience constructor for creating new literary works
     */
    public Book(String workTitle, String internationalCode, Author creator) {
        this.title = workTitle;
        this.isbn = internationalCode;
        this.creator = creator;
    }
}
