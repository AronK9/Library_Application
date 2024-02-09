package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "publisher_id", nullable = false)
    private Long publisherId;

    private String name;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<Book> publishedBooks;

    private String address;

    private String website;


}
