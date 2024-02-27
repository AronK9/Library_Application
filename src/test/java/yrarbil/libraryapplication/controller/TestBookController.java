package yrarbil.libraryapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import yrarbil.libraryapplication.model.Author;
import yrarbil.libraryapplication.model.Book;
import yrarbil.libraryapplication.model.Publisher;
import yrarbil.libraryapplication.model.enums.BookStatus;
import yrarbil.libraryapplication.model.enums.Format;
import yrarbil.libraryapplication.model.enums.Genre;
import yrarbil.libraryapplication.service.BookService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TestBookController {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book1 = new Book( new Publisher("publisher1", new ArrayList<>(), "address", "website"),
                "title", new Author("name", LocalDate.parse("1994-10-10"), "bio", new ArrayList<>()),
                Genre.ART, Format.HARDCOVER, 110, BookStatus.BORROWED
                );

        Book book2 = new Book( new Publisher("publisher2", new ArrayList<>(), "address2", "website2"),
                "title", new Author("name2", LocalDate.parse("1994-06-10"), "bio", new ArrayList<>()),
                Genre.CRIME, Format.DIGITAL, 170, BookStatus.AVAILABLE
        );

        List<Book> bookList = Arrays.asList(book1, book2);

        Mockito.when(bookService.getAllBooks()).thenReturn(bookList);

        mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$",  Matchers.hasSize(2)));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book();
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));
    }

    @Test
    public void testGetBookByNonExistentId () throws Exception {
        when (bookService.getBookById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = new Book();
        book.setPublisher(new Publisher());
        book.setTitle("title");
        book.setAuthor(new Author());
        book.setGenre(Genre.BIOGRAPHY);
        book.setFormat(Format.HARDCOVER);
        book.setNumberOfPages(200);
        book.setBookStatus(BookStatus.AVAILABLE);

        when(bookService.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));
    }

    @Test
    public void testAddInvalidBook() throws Exception {
        Book invalidBook = new Book();
        invalidBook.setTitle("");

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest());
    }

}
