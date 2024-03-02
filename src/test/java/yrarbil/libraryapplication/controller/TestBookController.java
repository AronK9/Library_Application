package yrarbil.libraryapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).
                setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();
    }

    @Test
    public void testGetAllBooks() throws Exception {

        Publisher publisher1 = new Publisher(1L, "publisher1", new ArrayList<>(), "address", "website");

        Author author1 = new Author(1L, "name", LocalDate.now(), "bio", new ArrayList<>());

        Book book1 = new Book(publisher1, "book1", author1, Genre.DYSTOPIAN, Format.PAPERBACK, 300, BookStatus.AVAILABLE);

        Book book2 = new Book(publisher1, "book2", author1, Genre.MYSTERY, Format.HARDCOVER, 400, BookStatus.BORROWED);


        List<Book> bookList = Arrays.asList(book1, book2);

        Mockito.when(bookService.getAllBooks()).thenReturn(bookList);

        mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[*].publisher.publisherId", Matchers.everyItem(Matchers.is(1))))
                .andExpect(jsonPath("$.[*].title", Matchers.contains(book1.getTitle(), book2.getTitle())))
                .andExpect(jsonPath("$.[*].author.authorId", Matchers.everyItem(Matchers.is(1))))
                .andExpect(jsonPath("$.[*].genre", Matchers.contains(book1.getGenre().toString(), book2.getGenre().toString())))
                .andExpect(jsonPath("$.[*].format", Matchers.contains(book1.getFormat().toString(), book2.getFormat().toString())))
                .andExpect(jsonPath("$.[*].numberOfPages", Matchers.contains(book1.getNumberOfPages(), book2.getNumberOfPages())))
                .andExpect(jsonPath("$.[*].bookStatus", Matchers.contains(book1.getBookStatus().toString(), book2.getBookStatus().toString())));
    }

    @Test
    public void testGetBookById() throws Exception {
        Publisher publisher1 = new Publisher(1L, "publisher1", new ArrayList<>(), "address", "website");

        Author author1 = new Author(1L, "name", LocalDate.now(), "bio", new ArrayList<>());

        Book book = new Book(publisher1, "book1", author1, Genre.DYSTOPIAN, Format.PAPERBACK, 300, BookStatus.AVAILABLE);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)))
                .andExpect(jsonPath("$.publisher.publisherId", Matchers.is(1)))
                .andExpect(jsonPath("$.title", Matchers.is(book.getTitle())))
                .andExpect(jsonPath("$.author.authorId", Matchers.is(1)))
                .andExpect(jsonPath("$.genre", Matchers.is(book.getGenre().toString())))
                .andExpect(jsonPath("$.format", Matchers.is(book.getFormat().toString())))
                .andExpect(jsonPath("$.numberOfPages", Matchers.is(book.getNumberOfPages())))
                .andExpect(jsonPath("$.bookStatus", Matchers.is(book.getBookStatus().toString())));

    }

    @Test
    public void testGetBookByNonExistentId() throws Exception {
        when(bookService.getBookById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddBook() throws Exception {
        Publisher publisher1 = new Publisher(1L, "publisher1", new ArrayList<>(), "address", "website");
        Author author1 = new Author(2L, "name", LocalDate.now(), "bio", new ArrayList<>());
        Book book = new Book(publisher1, "book1", author1, Genre.DYSTOPIAN, Format.PAPERBACK, 300, BookStatus.AVAILABLE);


        when(bookService.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(book)))
                .andExpect(jsonPath("$.publisher.publisherId", Matchers.is(1)))
                .andExpect(jsonPath("$.title", Matchers.is(book.getTitle())))
                .andExpect(jsonPath("$.author.authorId", Matchers.is(2)))
                .andExpect(jsonPath("$.genre", Matchers.is(book.getGenre().toString())))
                .andExpect(jsonPath("$.format", Matchers.is(book.getFormat().toString())))
                .andExpect(jsonPath("$.numberOfPages", Matchers.is(book.getNumberOfPages())))
                .andExpect(jsonPath("$.bookStatus", Matchers.is(book.getBookStatus().toString())));
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

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistentBook() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(bookService).delete(anyLong());

        mockMvc.perform(delete("/book/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book(new Publisher(), "title", new Author(), Genre.BIOGRAPHY, Format.PAPERBACK, 646, BookStatus.IN_REPAIR);

        when(bookService.updateById(1L, book)).thenReturn(Optional.of(book));

        mockMvc.perform(put("/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)))
                .andExpect(jsonPath("$.title", Matchers.is(book.getTitle())))
                .andExpect(jsonPath("$.genre", Matchers.is(book.getGenre().toString())))
                .andExpect(jsonPath("$.format", Matchers.is(book.getFormat().toString())))
                .andExpect(jsonPath("$.numberOfPages", Matchers.is(book.getNumberOfPages())))
                .andExpect(jsonPath("$.bookStatus", Matchers.is(book.getBookStatus().toString())));
    }

    @Test
    public void testUpdateNonExistentAuthor() throws Exception {
        Book book = new Book(new Publisher(), "title", new Author(), Genre.BIOGRAPHY, Format.PAPERBACK, 646, BookStatus.IN_REPAIR);

        when(bookService.updateById(anyLong(), any(Book.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isNotFound());

    }

}
