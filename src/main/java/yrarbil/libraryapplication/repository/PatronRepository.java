package yrarbil.libraryapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yrarbil.libraryapplication.model.Patron;
@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {
}
