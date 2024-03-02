package yrarbil.libraryapplication.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yrarbil.libraryapplication.model.Book;
import yrarbil.libraryapplication.service.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity <List<Book>> getAllBooks() {
        List <Book> books = bookService.getAllBooks();

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById (@PathVariable Long id) {

        Optional<Book> book = bookService.getBookById(id);

        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {

        Book createdBook = bookService.save(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook (@Valid @PathVariable Long id) {
        try {
            bookService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook (@RequestBody Book newBook, @PathVariable Long id) {
        Optional<Book> updatedBook = bookService.updateById(id, newBook);

        if (updatedBook.isPresent()) {
            return new ResponseEntity<>(updatedBook.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
