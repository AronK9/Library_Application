package yrarbil.libraryapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yrarbil.libraryapplication.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
