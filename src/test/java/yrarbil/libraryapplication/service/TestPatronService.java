package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import yrarbil.libraryapplication.model.Patron;
import yrarbil.libraryapplication.repository.PatronRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TestPatronService {

    @InjectMocks
    PatronService patronService;

    @Mock
    PatronRepository patronRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllPatrons_thenReturnListOfPatrons() {

        List<Patron> patrons = new ArrayList<>();
        Mockito.when(patronRepository.findAll()).thenReturn(patrons);

        List<Patron> result = patronService.getAllPatrons();
        assertEquals(patrons, result);
    }

    @Test
    public void whenGetAllPatrons_andNoPatronsPresent_thenReturnEmptyList() {
        Mockito.when(patronRepository.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    public void whenGetPatronById_thenReturnPatronOptional() {
        Long id = 1L;
        Patron patron = new Patron();
        Mockito.when(patronRepository.findById(id)).thenReturn(Optional.of(patron));
    }

    @Test
    public void whenGetPatronById_andPatronDoesNotExist_thenReturnEmptyOptional() {
        Long id = 1L;
        Mockito.when(patronRepository.findById(id)).thenReturn(Optional.empty());
    }

    @Test
    public void whenSavePatron_thenReturnSavedPatron() {
        Patron patron = new Patron();
        Mockito.when(patronRepository.save(patron)).thenReturn(patron);

        Patron result = patronService.save(patron);
        assertEquals(patron, result);
    }

    @Test
    public void whenSavePatron_andPatronIsNull_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> patronService.save(null));
    }

    @Test
    public void WhenUpdatePatronById_thenReturnsUpdatedPatron() {

        Long patronId = 1L;
        Patron oldPatron = new Patron();
        oldPatron.setPatronId(patronId);
        oldPatron.setName("Old Patron");
        oldPatron.setAge(22);

        Patron newPatron = new Patron();
        newPatron.setName("New Patron");
        newPatron.setAge(28);

        when(patronRepository.findById(eq(patronId))).thenReturn(Optional.of(oldPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(newPatron);

        Optional<Patron> actualPatron = patronService.updateById(patronId, newPatron);

        assertEquals(newPatron, actualPatron.get());
    }

    @Test
    public void whenUpdatePatron_andPatronDoesNotExist_thenReturnEmptyOptional(){

        Long patronId = 1L;
        Patron patron = new Patron();
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        Optional<Patron> actualPatron = patronService.updateById(patronId, patron);

        assertEquals(Optional.empty(), actualPatron);
    }

    @Test
    public void whenDeletePatron_thenRepositoryMethodIsCalled(){

        Long patronId = 1L;

        when(patronRepository.existsById(any(Long.class))).thenReturn(true);

        patronService.delete(patronId);

        verify(patronRepository, atLeastOnce()).deleteById(eq(patronId));
    }

    @Test
    public void whenDeletePatron_andPatronDoesNotExist_thenThrowsResponseStatusException(){

        Long patronId = 1L;

        when(patronRepository.existsById(patronId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            patronService.delete(patronId);
        });
    }
}
