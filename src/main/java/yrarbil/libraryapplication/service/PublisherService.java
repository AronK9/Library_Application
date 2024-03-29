package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yrarbil.libraryapplication.model.Publisher;
import yrarbil.libraryapplication.repository.PublisherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Transactional
    public Optional<Publisher> getPublisherById(Long id) {
        return publisherRepository.findById(id);
    }

    @Transactional
    public Publisher save(Publisher publisher) {
        if (publisher == null)
            throw new IllegalArgumentException("Author cannot be null");
        return publisherRepository.save(publisher);
    }

    public void delete(Long id) {
        if(publisherRepository.existsById(id)) {
            publisherRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Publisher with id " + id + " does not exist!");
        }
    }

    public Optional<Publisher> updateById(Long id, Publisher updatedPublisher) {

        Optional<Publisher> optionalPublisher = publisherRepository.findById(id);

        if (optionalPublisher.isPresent()) {
            Publisher publisher = optionalPublisher.get();

            publisher.setName(updatedPublisher.getName());
            publisher.setPublishedBooks(updatedPublisher.getPublishedBooks());
            publisher.setAddress(updatedPublisher.getAddress());
            publisher.setWebsite(updatedPublisher.getWebsite());

            publisher = publisherRepository.save(publisher);
            return Optional.of(publisher);
        }
        return Optional.empty();
    }
}
