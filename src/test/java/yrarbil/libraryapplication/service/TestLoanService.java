package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import yrarbil.libraryapplication.model.Loan;
import yrarbil.libraryapplication.model.Patron;
import yrarbil.libraryapplication.repository.LoanRepository;
import yrarbil.libraryapplication.repository.PatronRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TestLoanService {

    @InjectMocks
    LoanService loanService;

    @InjectMocks
    PatronService patronService;

    @Mock
    LoanRepository loanRepository;

    @Mock
    PatronRepository patronRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllLoans_thenReturnListOfLoans() {

        List<Loan> expectedLoan = new ArrayList<>();
        expectedLoan.add(new Loan());

        when(loanRepository.findAll()).thenReturn(expectedLoan);

        List<Loan> actualLoan = loanService.getAllLoans();

        assertEquals(expectedLoan, actualLoan);
    }

    @Test
    public void whenGetAllLoans_andNoLoansPresent_thenReturnEmptyList() {

        when(loanRepository.findAll()).thenReturn(new ArrayList<>());

        List<Loan> actualLoans = loanService.getAllLoans();

        assertTrue(actualLoans.isEmpty());
    }

    @Test
    public void whenGetLoanById_thenReturnOptionalLoan() {
        Long loanId = 1L;
        Loan expectedLoan = new Loan();
        expectedLoan.setLoanId(loanId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(expectedLoan));

        Optional<Loan> actualLoan = loanService.getLoanById(loanId);

        assertEquals(Optional.of(expectedLoan), actualLoan);
    }

    @Test
    public void whenGetLoanById_andNoLoanPresent_thenReturnEmptyOptional() {

        Long loanId = 1L;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Optional<Loan> actualLoan = loanService.getLoanById(loanId);

        assertEquals(Optional.empty(), actualLoan);
    }

    @Test
    public void whenSaveLoan_thenReturnSavedLoan() {
        Loan loan = new Loan();
        loan.setLoanId(1L);

        Patron patron = new Patron();
        patron.setPatronId(2L);

        loan.setPatron(patron);

        when(loanRepository.save(loan)).thenReturn(loan);
        when(patronRepository.findById(anyLong()))
                .thenReturn(Optional.of(patron));

        Loan savedLoan = loanService.save(loan);

        assertEquals(loan, savedLoan);
    }

    @Test
    public void whenSaveLoan_andLoanIsNull_thenThrowsException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> loanService.save(null));
    }

    @Test
    public void whenDeleteLoan_thenLoanIsDeleted() {

        Long loanId = 1L;
        Loan loan = new Loan();

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        doNothing().when(loanRepository).deleteById(loanId);

        loanService.delete(loanId);

        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    public void whenDeleteLoan_butLoanIsNull_thenLoanIsDeleted() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> loanService.delete(null));
    }


    @Test
    public void whenUpdateAuthorById_thenReturnsUpdatedAuthor() {

        Long loanId = 1L;
        Loan updatedLoan = new Loan();
        updatedLoan.setLoanId(loanId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(updatedLoan));
        when(loanRepository.save(updatedLoan)).thenReturn(updatedLoan);

        Optional<Loan> actualLoan = loanService.updateById(loanId, updatedLoan);

        assertEquals(Optional.of(updatedLoan), actualLoan);
    }

    @Test
    public void whenUpdateAuthor_andAuthorDoesNotExist_thenReturnEmptyOptional() {

        Long loanId = 1L;
        Loan loan = new Loan();

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Optional<Loan> actualLoan = loanService.updateById(loanId, loan);

        assertEquals(Optional.empty(), actualLoan);

    }
}
