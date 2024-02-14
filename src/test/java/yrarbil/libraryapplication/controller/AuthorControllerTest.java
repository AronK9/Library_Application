package yrarbil.libraryapplication.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
import yrarbil.libraryapplication.service.AuthorService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    @Mock
    private AuthorService authorService;
    @InjectMocks
    private AuthorController authorController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    @Test
    public void GetAllAuthorsTest() throws Exception {
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
    public void testAddAuthor() throws Exception {
        Author author = new Author();
        when(authorService.save(author)).thenReturn(author);

        mockMvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/author/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        Author author = new Author();
        when(authorService.updateById(1L, author)).thenReturn(Optional.of(author));

        mockMvc.perform(put("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));
    }
}



