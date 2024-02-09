package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "patron")
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patron_id", nullable = false)
    private Long PatronId;

    private String username;

    private String password;

    private String name;

    private int age;

    @OneToMany(mappedBy = "patron", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonManagedReference
    private List<Loan> loanHistory;


}
