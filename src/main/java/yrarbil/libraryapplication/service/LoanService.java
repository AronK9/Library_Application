package yrarbil.libraryapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yrarbil.libraryapplication.model.Loan;
import yrarbil.libraryapplication.repository.LoanRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    @Transactional
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    public void delete(Long id) {
        loanRepository.deleteById(id);
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
