package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import yrarbil.libraryapplication.model.Author;
import yrarbil.libraryapplication.repository.AuthorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TestAuthorService {

    @InjectMocks
    AuthorService authorService;

    @Mock
    AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllAuthors_thenReturnListOfAuthors(){

        List<Author> expectedAuthors = new ArrayList<>();
        expectedAuthors.add(new Author());

        when(authorRepository.findAll()).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorService.getAllAuthors();

        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    public void whenGetAllAuthors_andNoAuthorsPresent_thenReturnEmptyList(){

        when(authorRepository.findAll()).thenReturn(new ArrayList<>());

        List<Author> actualAuthors = authorService.getAllAuthors();

        assertTrue(actualAuthors.isEmpty());
    }

    @Test
    public void whenGetAuthorById_thenReturnAuthorOptional(){

        Long authorId = 1L;
        Author expectedAuthor = new Author();
        expectedAuthor.setAuthorId(authorId);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(expectedAuthor));

        Optional<Author> actualAuthor = authorService.getAuthorById(authorId);

        assertEquals(Optional.of(expectedAuthor), actualAuthor);
    }

    @Test
    public void whenGetAuthorById_andNoAuthorPresent_thenReturnEmptyOptional(){

        Long authorId = 1L;

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Optional<Author> actualAuthor = authorService.getAuthorById(authorId);

        assertEquals(Optional.empty(), actualAuthor);
    }

    @Test
    public void whenSaveAuthor_thenReturnsSavedAuthor(){

        Author savedAuthor = new Author();
        savedAuthor.setName("Author Name");
        savedAuthor.setDateOfBirth(LocalDate.now());

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        Author actualAuthor = authorService.save(savedAuthor);

        assertEquals(savedAuthor, actualAuthor);
    }

    @Test
    public void whenSaveAuthor_andAuthorIsNull_thenThrowsException(){

        Assertions.assertThrows(IllegalArgumentException.class, () -> authorService.save(null));
    }

    @Test
    public void whenUpdateAuthorById_thenReturnsUpdatedAuthor(){
        Long authorId = 1L;
        Author oldAuthor = new Author();
        oldAuthor.setAuthorId(authorId);
        oldAuthor.setName("Old Author");
        oldAuthor.setDateOfBirth(LocalDate.now());

        Author newAuthor = new Author();
        newAuthor.setName("New Author");
        newAuthor.setDateOfBirth(LocalDate.now().minusDays(10));

        when(authorRepository.findById(eq(authorId))).thenReturn(Optional.of(oldAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);

        Optional<Author> actualAuthor = authorService.updateById(authorId, newAuthor);

        assertEquals(newAuthor, actualAuthor.get());
    }

    @Test
    public void whenUpdateAuthor_andAuthorDoesNotExist_thenReturnEmptyOptional(){

        Long authorId = 1L;
        Author author = new Author();

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Optional<Author> actualAuthor = authorService.updateById(authorId, author);

        assertEquals(Optional.empty(), actualAuthor);
    }

    @Test
    public void whenDeleteAuthor_thenRepositoryMethodIsCalled(){
        Long authorId = 1L;

        when(authorRepository.existsById(any(Long.class))).thenReturn(true);

        authorService.delete(authorId);

        verify(authorRepository, atLeastOnce()).deleteById(eq(authorId));
    }

        @Test
        public void whenDeleteAuthor_andAuthorDoesNotExist_thenThrowsResponseStatusException(){

        Long authorId = 1L;

        when(authorRepository.existsById(authorId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            authorService.delete(authorId);
        });
    }
}
