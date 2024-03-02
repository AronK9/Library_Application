package yrarbil.libraryapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yrarbil.libraryapplication.model.Patron;
import yrarbil.libraryapplication.service.PatronService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class TestPatronController {

    @Mock
    private PatronService patronService;
    @InjectMocks
    private PatronController patronController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(patronController).build();
    }

    @Test
    public void testGetAllPatrons() throws Exception {

        Patron patron1 = new Patron("username", "name", 22, new ArrayList<>());

        Patron patron2 = new Patron("username2","name2", 32, new ArrayList<>());

        List<Patron> patronList = Arrays.asList(patron1, patron2);

        Mockito.when(patronService.getAllPatrons()).thenReturn(patronList);

        mockMvc.perform(MockMvcRequestBuilders.get("/patron"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].username", Matchers.is(patron1.getUsername())))
                .andExpect(jsonPath("$[0].name", Matchers.is(patron1.getName())))
                .andExpect(jsonPath("$[0].age", Matchers.is(patron1.getAge())))
                .andExpect(jsonPath("$[1].username", Matchers.is(patron2.getUsername())))
                .andExpect(jsonPath("$[1].name", Matchers.is(patron2.getName())))
                .andExpect(jsonPath("$[1].age", Matchers.is(patron2.getAge())));
    }

    @Test
    public void testGetPatronById() throws Exception {
        Patron patron = new Patron();
        when(patronService.getPatronById(1L)).thenReturn(Optional.of(patron));

        mockMvc.perform(get("/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patron)))
                .andExpect(jsonPath("$.username", is(patron.getUsername())))
                .andExpect(jsonPath("$.name", is(patron.getName())))
                .andExpect(jsonPath("$.age", is(patron.getAge())));
    }

    @Test
    public void testGetPatronByNonExistentId() throws Exception {
        when(patronService.getPatronById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddPatron() throws Exception {
        Patron patron = new Patron("username", "name", 22, new ArrayList<>());
        when(patronService.save(any(Patron.class))).thenReturn(patron);

        mockMvc.perform(post("/patron")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(patron)))
                .andExpect(jsonPath("$.username", is(patron.getUsername())))
                .andExpect(jsonPath("$.name", is(patron.getName())))
                .andExpect(jsonPath("$.age", is(patron.getAge())));
    }

    @Test
    public void testAddInvalidPatron() throws Exception {

        Patron invalidPatron = new Patron(null, null, null, 22, new ArrayList<>());

        mockMvc.perform(post("/patron")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatron)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeletePatron() throws Exception {

        mockMvc.perform(delete("/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistentPatron() throws Exception {

        doThrow(new EmptyResultDataAccessException(1)).when(patronService).delete(anyLong());

        mockMvc.perform(delete("/patron/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePatron() throws Exception {

        Patron updatedPatron = new Patron("newUsername",  "newName", 25, new ArrayList<>());
        when(patronService.updateById(eq(1L), any(Patron.class))).thenReturn(Optional.of(updatedPatron));

        mockMvc.perform(put("/patron/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatron)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedPatron)))
                .andExpect(jsonPath("$.username", is(updatedPatron.getUsername())))
                .andExpect(jsonPath("$.name", is(updatedPatron.getName())))
                .andExpect(jsonPath("$.age", is(updatedPatron.getAge())));
    }

    @Test
    public void testUpdateNonExistingPatron() throws Exception {

        Patron patron = new Patron("username", "name", 22, new ArrayList<>());


        when(patronService.updateById(anyLong(), any(Patron.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/patron/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(status().isNotFound());
    }
}
