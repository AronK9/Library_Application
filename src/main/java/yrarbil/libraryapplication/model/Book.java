package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import yrarbil.libraryapplication.model.enums.BookStatus;
import yrarbil.libraryapplication.model.enums.Format;
import yrarbil.libraryapplication.model.enums.Genre;

import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "book")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bookId")
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

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Format format;

    @Column(name = "number_of_pages")
    private int numberOfPages;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    public Book(Publisher publisher, String title, Author author, Genre genre, Format format, int numberOfPages, BookStatus bookStatus) {
        this.publisher = publisher;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.format = format;
        this.numberOfPages = numberOfPages;
        this.bookStatus = bookStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return getNumberOfPages() == book.getNumberOfPages() && Objects.equals(getBookId(), book.getBookId()) && Objects.equals(getPublisher(), book.getPublisher()) && Objects.equals(getTitle(), book.getTitle()) && Objects.equals(getAuthor(), book.getAuthor()) && getGenre() == book.getGenre() && getFormat() == book.getFormat() && getBookStatus() == book.getBookStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId(), getPublisher(), getTitle(), getAuthor(), getGenre(), getFormat(), getNumberOfPages(), getBookStatus());
    }
}
