package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;
import yrarbil.libraryapplication.model.enums.LoanStatus;

import java.time.LocalDate;
import java.util.Objects;

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

    private LocalDate loanDate;

    private LocalDate requiredReturnDate;

    @Nullable
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    public Loan(Patron patron, LocalDate loanDate, LocalDate requiredReturnDate, LocalDate actualReturnDate, LoanStatus loanStatus) {
        this.patron = patron;
        this.loanDate = loanDate;
        this.requiredReturnDate = requiredReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.loanStatus = loanStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(getLoanId(), loan.getLoanId()) && Objects.equals(getPatron(), loan.getPatron()) && Objects.equals(getLoanDate(), loan.getLoanDate()) && Objects.equals(getRequiredReturnDate(), loan.getRequiredReturnDate()) && Objects.equals(getActualReturnDate(), loan.getActualReturnDate()) && getLoanStatus() == loan.getLoanStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLoanId(), getPatron(), getLoanDate(), getRequiredReturnDate(), getActualReturnDate(), getLoanStatus());
    }
}
