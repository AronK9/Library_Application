package yrarbil.libraryapplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "author")

public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String biography;

    @ManyToMany
    @ToString.Exclude
    private List<Book> listOfBooks;

}
