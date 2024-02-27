package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "author")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "authorId")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "author_id", nullable = false)
    private Long authorId;


    @NotEmpty
    @NotNull
    private String name;

    @Column(name = "date_of_birth")
    @NotNull
    private LocalDate dateOfBirth;

    private String biography;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    @ToString.Exclude
    private List<Book> listOfBooks;

    public Author(String name, LocalDate dateOfBirth, String biography, List<Book> listOfBooks) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.biography = biography;
        this.listOfBooks = listOfBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(getAuthorId(), author.getAuthorId()) && Objects.equals(getName(), author.getName()) && Objects.equals(getDateOfBirth(), author.getDateOfBirth()) && Objects.equals(getBiography(), author.getBiography()) && Objects.equals(getListOfBooks(), author.getListOfBooks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthorId(), getName(), getDateOfBirth(), getBiography(), getListOfBooks());
    }
}
