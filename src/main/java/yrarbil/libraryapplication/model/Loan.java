package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import yrarbil.libraryapplication.model.enums.LoanStatus;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "loan_id", nullable = false)
    private Long loanId;


    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Patron patron;

    @Temporal(TemporalType.DATE)
    private LocalDate loanDate;

    @Temporal(TemporalType.DATE)
    private LocalDate requiredReturnDate;

    @Temporal(TemporalType.DATE)
    private LocalDate actualReturnDate;

    private LoanStatus loanStatus;
}
