package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import yrarbil.libraryapplication.model.Publisher;
import yrarbil.libraryapplication.repository.PublisherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TestPublisherService {

    @InjectMocks
    PublisherService publisherService;

    @Mock
    PublisherRepository publisherRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllPublishers_thenReturnListOfPublishers() {

        List<Publisher> publishers = new ArrayList<>();
        Mockito.when(publisherRepository.findAll()).thenReturn(publishers);

        List<Publisher> result = publisherService.getAllPublishers();
        assertEquals(publishers, result);
    }

    @Test
    public void whenGetAllPublishers_andNoPublishersPresent_thenReturnEmptyList() {

        when(publisherRepository.findAll()).thenReturn(new ArrayList<>());

        List<Publisher> actualPublishers = publisherService.getAllPublishers();

        assertTrue(actualPublishers.isEmpty());
    }

    @Test
    public void whenGetPublishersById_thenReturnPublisherOptional() {

        Long id = 1L;
        Publisher publisher = new Publisher();
        publisher.setPublisherId(id);
        Optional<Publisher> expectedPublisher = Optional.of(publisher);

        when(publisherRepository.findById(id)).thenReturn(expectedPublisher);

        Optional<Publisher> actualPublisher = publisherService.getPublisherById(id);

        assertEquals(expectedPublisher, actualPublisher);
    }

    @Test
    public void whenGetPublisherById_andNoPublisherPresent_thenReturnEmptyOptional() {
        Long id = 1L;
        Optional<Publisher> expectedPublisher = Optional.empty();

        when(publisherRepository.findById(id)).thenReturn(expectedPublisher);

        Optional<Publisher> actualPublisher = publisherService.getPublisherById(id);

        assertEquals(expectedPublisher, actualPublisher);
    }

    @Test
    public void whenSavePublisher_thenReturnsSavedPublisher() {

        Publisher savedPublisher = new Publisher();
        savedPublisher.setName("Publisher Name");
        savedPublisher.setAddress("Address");

        when(publisherRepository.save(any(Publisher.class))).thenReturn(savedPublisher);

        Publisher actualPublisher = publisherService.save(savedPublisher);

        assertEquals(savedPublisher, actualPublisher);
    }

    @Test
    public void whenSavePublisher_andPublisherIsNull_thenThrowsException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> publisherService.save(null));
    }

    @Test
    public void whenUpdatePublisherById_thenReturnsUpdatedPublisher() {

        Long id = 1L;
        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setName("Updated Publisher Name");
        updatedPublisher.setAddress("Updated Address");

        Optional<Publisher> expectedPublisher = Optional.of(updatedPublisher);

        when(publisherRepository.findById(id)).thenReturn(Optional.of(new Publisher()));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(updatedPublisher);

        Optional<Publisher> actualPublisher = publisherService.updateById(id, updatedPublisher);

        assertEquals(expectedPublisher, actualPublisher);
    }

    @Test
    public void whenUpdatePublisher_andPublisherDoesNotExist_thenReturnEmptyOptional() {
        Long id = 1L;
        Optional<Publisher> expectedPublisher = Optional.empty();

        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Publisher> actualPublisher = publisherService.updateById(id, new Publisher());

        assertEquals(expectedPublisher, actualPublisher);
    }

    @Test
    public void whenDeletePublisher_thenRepositoryMethodIsCalled() {

        Long publisherId = 1L;

        when(publisherRepository.existsById(any(Long.class))).thenReturn(true);

        publisherService.delete(publisherId);

        verify(publisherRepository, atLeastOnce()).deleteById(eq(publisherId));
    }

    @Test
    public void whenDeletePublisher_andPublisherDoesNotExist_thenThrowsResponseStatusException() {

        Long publisherId = 1L;

        when(publisherRepository.existsById(publisherId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            publisherService.delete(publisherId);
        });    }

}
