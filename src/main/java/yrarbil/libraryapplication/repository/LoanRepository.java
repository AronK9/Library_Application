package yrarbil.libraryapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yrarbil.libraryapplication.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository <Loan, Long> {
}
