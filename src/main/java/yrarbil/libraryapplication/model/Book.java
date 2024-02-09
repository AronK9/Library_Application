package yrarbil.libraryapplication.model;

import jakarta.persistence.*;
import lombok.*;
import yrarbil.libraryapplication.model.enums.BookStatus;
import yrarbil.libraryapplication.model.enums.Format;
import yrarbil.libraryapplication.model.enums.Genre;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    @ToString.Exclude
    private Publisher publisher;


    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private Author author;

    private Genre genre;

    private Format format;

    @Column(name = "number_of_pages")
    private int numberOfPages;

    private BookStatus bookStatus;

}
