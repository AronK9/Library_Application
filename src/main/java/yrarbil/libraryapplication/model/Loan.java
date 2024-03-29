package yrarbil.libraryapplication.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;
import yrarbil.libraryapplication.model.enums.LoanStatus;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "loan")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "loanId")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "loan_id", nullable = false)
    private Long loanId;


    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    @ToString.Exclude
    @NotNull
    private Patron patron;

    @NotNull
    private LocalDate loanDate;

    @NotNull
    private LocalDate requiredReturnDate;

    @Nullable
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoanStatus loanStatus;

    public Loan(Patron patron, LocalDate loanDate, LocalDate requiredReturnDate, @Nullable LocalDate actualReturnDate, LoanStatus loanStatus) {
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
