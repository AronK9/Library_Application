package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yrarbil.libraryapplication.model.Book;
import yrarbil.libraryapplication.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book save(Book book) {

        if (book == null)
            throw new IllegalArgumentException("Book can not be null");
        return bookRepository.save(book);

    }

    public void delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Book with id " + id + " does not exist!");
        }
    }

    public Optional<Book> updateById(Long id, Book updatedBook) {

        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            book.setPublisher(updatedBook.getPublisher());
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setGenre(updatedBook.getGenre());
            book.setFormat(updatedBook.getFormat());
            book.setNumberOfPages(updatedBook.getNumberOfPages());
            book.setBookStatus(updatedBook.getBookStatus());

            Book savedBook = bookRepository.save(book);
            return Optional.of(savedBook);

        }
        return Optional.empty();
    }
}
