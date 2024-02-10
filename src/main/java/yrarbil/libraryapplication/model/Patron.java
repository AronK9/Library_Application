package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "patron")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "patronId")
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patron_id", nullable = false)
    private Long patronId;

    private String username;

    private String password;

    private String name;

    private int age;

    @OneToMany(mappedBy = "patron", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Loan> loanHistory;

    public Patron(String username, String password, String name, int age, List<Loan> loanHistory) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.loanHistory = loanHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return getAge() == patron.getAge() && Objects.equals(getPatronId(), patron.getPatronId()) && Objects.equals(getUsername(), patron.getUsername()) && Objects.equals(getPassword(), patron.getPassword()) && Objects.equals(getName(), patron.getName()) && Objects.equals(getLoanHistory(), patron.getLoanHistory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPatronId(), getUsername(), getPassword(), getName(), getAge(), getLoanHistory());
    }
}
