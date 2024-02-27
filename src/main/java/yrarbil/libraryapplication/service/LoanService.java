package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yrarbil.libraryapplication.model.Loan;
import yrarbil.libraryapplication.model.Patron;
import yrarbil.libraryapplication.repository.LoanRepository;
import yrarbil.libraryapplication.repository.PatronRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, PatronRepository patronRepository) {
        this.loanRepository = loanRepository;
        this.patronRepository = patronRepository;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public Loan save(Loan loan) {
        Patron patron = patronRepository.findById(loan.getPatron().getPatronId()).orElseThrow(() -> new RuntimeException("Patron not found"));
        loan.setPatron(patron);
        return loanRepository.save(loan);
    }

    public void delete(Long id) {
        if(loanRepository.existsById(id)) {
            loanRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Loan with id " + id + " does not exist!");
        }
    }

    public Optional<Loan> updateById(Long id, Loan updatedLoan) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);

        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();

            loan.setPatron(updatedLoan.getPatron());
            loan.setLoanDate(updatedLoan.getLoanDate());
            loan.setRequiredReturnDate(updatedLoan.getRequiredReturnDate());
            loan.setActualReturnDate(updatedLoan.getActualReturnDate());
            loan.setLoanStatus(updatedLoan.getLoanStatus());

            loan = loanRepository.save(loan);
            return Optional.of(loan);
        }
        return Optional.empty();
    }
}
