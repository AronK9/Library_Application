package yrarbil.libraryapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yrarbil.libraryapplication.model.Author;
import yrarbil.libraryapplication.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    public Optional<Author> updateById(Long id, Author updatedAuthor) {

        Optional<Author> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();

            author.setName(updatedAuthor.getName());
            author.setDateOfBirth(updatedAuthor.getDateOfBirth());
            author.setBiography(updatedAuthor.getBiography());
            author.setListOfBooks(updatedAuthor.getListOfBooks());

            author = authorRepository.save(author);
            return Optional.of(author);
        }
        return Optional.empty();
    }
}
