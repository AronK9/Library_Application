package yrarbil.libraryapplication.service;

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
        return publisherRepository.save(publisher);
    }

    public void delete(Long id) {
        publisherRepository.deleteById(id);
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
