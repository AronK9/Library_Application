package yrarbil.libraryapplication.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yrarbil.libraryapplication.model.Author;
import yrarbil.libraryapplication.service.AuthorService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class TestAuthorController {

    @Mock
    private AuthorService authorService;
    @InjectMocks
    private AuthorController authorController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    @Test
    public void testGetAllAuthors() throws Exception {
        Author author1 = new Author("author1", LocalDate.parse("1994-12-23"), "bio1", new ArrayList<>());

        Author author2 = new Author("author2", LocalDate.parse("1994-12-24"), "bio2", new ArrayList<>());

        List<Author> authorsList = Arrays.asList(author1, author2);

        Mockito.when(authorService.getAllAuthors()).thenReturn(authorsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/author"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

    }

    @Test
    public void testGetAuthorById() throws Exception {
        Author author = new Author();
        when(authorService.getAuthorById(1L)).thenReturn(Optional.of(author));

        mockMvc.perform(get("/author/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));
    }

    @Test
    public void testGetAuthorByNonExistentId () throws Exception {
        when (authorService.getAuthorById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/author/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddAuthor() throws Exception {
        Author author = new Author();
        author.setName("author");
        author.setDateOfBirth(LocalDate.now());

        when(authorService.save(any(Author.class))).thenReturn(author);

        mockMvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));
    }

    @Test
    public void testAddInvalidAuthor() throws Exception {
        Author invalidAuthor = new Author();
        invalidAuthor.setName("");

        mockMvc.perform(post("/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAuthor)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/author/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistentAuthor() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(authorService).delete(anyLong());

        mockMvc.perform(delete("/author/1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        Author author = new Author("name", LocalDate.parse("1995-05-05"), "bio", new ArrayList<>());

        when(authorService.updateById(1L, author)).thenReturn(Optional.of(author));

        mockMvc.perform(put("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));
    }

    @Test
    public void testUpdateNonExistentAuthor() throws Exception {
        Author author = new Author("name", LocalDate.parse("1995-05-05"), "bio", new ArrayList<>());

        when(authorService.updateById(anyLong(), any(Author.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/author/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isNotFound());

    }
}



