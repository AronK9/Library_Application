package yrarbil.libraryapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yrarbil.libraryapplication.service.PublisherService;
import yrarbil.libraryapplication.model.Publisher;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TestPublisherController {

    @Mock
    private PublisherService publisherService;
    @InjectMocks
    private PublisherController publisherController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController).build();

    }

    @Test
    public void testGetAllPublishers() throws Exception {
        List<Publisher> publishers = new ArrayList<>();

        publishers.add(new Publisher("publisher1", new ArrayList<>(), "address", "website"));
        publishers.add(new Publisher("publisher2", new ArrayList<>(), "address2", "website2"));
        Mockito.when(publisherService.getAllPublishers()).thenReturn(publishers);

        mockMvc.perform(MockMvcRequestBuilders.get("/publisher"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void testGetPublisherById() throws Exception {
        Long id = 1L;
        Publisher publisher = new Publisher("publisher1", new ArrayList<>(), "address", "website");
        Mockito.when(publisherService.getPublisherById(id)).thenReturn(Optional.of(publisher));

        mockMvc.perform(MockMvcRequestBuilders.get("/publisher/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("publisher1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("address")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.website", Matchers.is("website")));
    }

    @Test
    public void testGetPublisherByNonExistentId() throws Exception {
        Long id = 100L;
        Mockito.when(publisherService.getPublisherById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/publisher/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testAddPublisher() throws Exception {

        Publisher publisher = new Publisher("publisher1", new ArrayList<>(), "address", "website");
        String requestBody = objectMapper.writeValueAsString(publisher);

        Mockito.when(publisherService.save(ArgumentMatchers.any(Publisher.class))).thenReturn(publisher);

        mockMvc.perform(MockMvcRequestBuilders.post("/publisher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("publisher1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("address")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.website", Matchers.is("website")));
    }

    @Test
    public void testAddInvalidPublisher() throws Exception {

        Publisher invalidPublisher = new Publisher();
        String requestBody = objectMapper.writeValueAsString(invalidPublisher);

        mockMvc.perform(MockMvcRequestBuilders.post("/publisher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDeletePublisher() throws Exception {

        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/publisher/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteNonExistentPublisher() throws Exception {

        doThrow(new EntityNotFoundException()).when(publisherService).delete(anyLong());

        mockMvc.perform(delete("/publisher/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePublisher() throws Exception {
        Long id = 1L;
        Publisher updatedPublisher = new Publisher("updatedPublisher", new ArrayList<>(), "updatedAddress", "updatedWebsite");
        String requestBody = objectMapper.writeValueAsString(updatedPublisher);

        Mockito.when(publisherService.updateById(eq(id), any(Publisher.class))).thenReturn(Optional.of(updatedPublisher));

        mockMvc.perform(MockMvcRequestBuilders.put("/publisher/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("updatedPublisher")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("updatedAddress")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.website", Matchers.is("updatedWebsite")));
    }

    @Test
    public void testUpdateNonExistentPublisher() throws Exception {

        Publisher publisher = new Publisher("publisher1", new ArrayList<>(), "address", "website");


        when(publisherService.updateById(anyLong(), any(Publisher.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/publisher/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisher)))
                .andExpect(status().isNotFound());
    }
}
