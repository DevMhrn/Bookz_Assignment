package com.example.books_crud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a literary creator in the system
 */
@Entity
@Table(name = "literary_creator")
@Getter
@Setter
@ToString(exclude = "literaryWorks")
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creatorId;

    @Column(name = "full_name", nullable = false)
    private String name;

    @Column(name = "biography", length = 2000)
    private String bio;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> literaryWorks = new HashSet<>();

    /**
     * Associates a literary work with this creator
     */
    public void linkLiteraryWork(Book literaryWork) {
        literaryWorks.add(literaryWork);
        literaryWork.setCreator(this);
    }

    /**
     * Removes association between a literary work and this creator
     */
    public void unlinkLiteraryWork(Book literaryWork) {
        literaryWorks.remove(literaryWork);
        literaryWork.setCreator(null);
    }
}
