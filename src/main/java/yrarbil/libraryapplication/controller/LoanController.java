package yrarbil.libraryapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yrarbil.libraryapplication.model.Loan;
import yrarbil.libraryapplication.service.LoanService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    @GetMapping
    public List<Loan> getAllLoans() {

        return loanService.getAllLoans();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById (@PathVariable Long id) {

        Optional<Loan> loan = loanService.getLoanById(id);

        if (loan.isPresent()) {
            return new ResponseEntity<>(loan.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Loan> addBook(@RequestBody Loan loan) {

        Loan createdLoan = loanService.save(loan);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan (@PathVariable Long id) {
        loanService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan (@RequestBody Loan newLoan, @PathVariable Long id) {
        Optional<Loan> updatedLoan = loanService.updateById(id, newLoan);

        if (updatedLoan.isPresent()) {
            return new ResponseEntity<>(updatedLoan.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
