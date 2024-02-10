package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "publisher")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "publisherId")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "publisher_id", nullable = false)
    private Long publisherId;

    private String name;

    @OneToMany(mappedBy = "publisher")
    @ToString.Exclude
    @JsonBackReference
    private List<Book> publishedBooks;

    private String address;

    private String website;

    public Publisher(String name, List<Book> publishedBooks, String address, String website) {
        this.name = name;
        this.publishedBooks = publishedBooks;
        this.address = address;
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return Objects.equals(getPublisherId(), publisher.getPublisherId()) && Objects.equals(getName(), publisher.getName()) && Objects.equals(getPublishedBooks(), publisher.getPublishedBooks()) && Objects.equals(getAddress(), publisher.getAddress()) && Objects.equals(getWebsite(), publisher.getWebsite());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPublisherId(), getName(), getPublishedBooks(), getAddress(), getWebsite());
    }
}
