package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import yrarbil.libraryapplication.model.Book;
import yrarbil.libraryapplication.model.enums.Genre;
import yrarbil.libraryapplication.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TestBookService {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllBooks_thenReturnListOfBooks() {

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book());

        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.getAllBooks();

        assertEquals(expectedBooks, actualBooks);
    }


    @Test
    public void whenGetAllBooks_andNoBooksPresent_thenReturnEmptyList() {

        when(bookRepository.findAll()).thenReturn(new ArrayList<>());

        List<Book> actualBooks = bookService.getAllBooks();

        assertTrue(actualBooks.isEmpty());
    }

    @Test
    public void whenGetBookById_thenReturnBookOptional() {

        Long bookId = 1L;
        Book expectedBook = new Book();
        expectedBook.setBookId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        Optional<Book> actualBook = bookService.getBookById(bookId);

        assertEquals(Optional.of(expectedBook), actualBook);
    }

    @Test
    public void whenGetBookById_andNoBookPresent_thenReturnEmptyOptional() {

        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Optional<Book> actualBook = bookService.getBookById(bookId);

        assertEquals(Optional.empty(), actualBook);
    }

    @Test
    public void whenSaveAuthor_thenReturnsSavedAuthor() {

        Book savedBook = new Book();
        savedBook.setTitle("Title");
        savedBook.setGenre(Genre.ADVENTURE);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Book actualBook = bookService.save(savedBook);

        assertEquals(savedBook, actualBook);
    }

    @Test
    public void whenSaveBook_andBookIsNull_thenThrowsException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.save(null));
    }

    @Test
    public void whenUpdateBookById_thenReturnsUpdatedBook() {
        Long bookId = 1L;
        Book oldBook = new Book();
        oldBook.setBookId(bookId);
        oldBook.setTitle("Old title");
        oldBook.setGenre(Genre.CRIME);

        Book newBook = new Book();
        newBook.setTitle("New Title");
        newBook.setGenre(Genre.BIOGRAPHY);

        when(bookRepository.findById(eq(bookId))).thenReturn(Optional.of(oldBook));
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        Optional<Book> actualBook = bookService.updateById(bookId, newBook);

        assertEquals(newBook, actualBook.get());
    }

    @Test
    public void whenUpdateBook_andBookDoesNotExist_thenReturnEmptyOptional() {

        Long bookId = 1L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Optional<Book> actualBook = bookService.updateById(bookId, book);

        assertEquals(Optional.empty(), actualBook);
    }

    @Test
    public void whenDeleteBook_thenRepositoryMethodIsCalled(){
        Long bookId = 1L;

        when(bookRepository.existsById(any(Long.class))).thenReturn(true);

        bookService.delete(bookId);

        verify(bookRepository, atLeastOnce()).deleteById(eq(bookId));
    }

    @Test
    public void whenDeleteBook_andBookDoesNotExist_thenThrowsResponseStatusException(){

        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.delete(bookId);
        });
    }
}
