package yrarbil.libraryapplication.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yrarbil.libraryapplication.model.Publisher;
import yrarbil.libraryapplication.service.PublisherService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        List<Publisher> publishers = publisherService.getAllPublishers();

        return new ResponseEntity<>(publishers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById (@PathVariable Long id) {

        Optional<Publisher> publisher = publisherService.getPublisherById(id);

        if (publisher.isPresent()) {
            return new ResponseEntity<>(publisher.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Publisher> addPublisher(@Valid @RequestBody Publisher publisher) {

        Publisher createdPublisher = publisherService.save(publisher);
        return new ResponseEntity<>(createdPublisher, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher (@Valid @PathVariable Long id) {
        try {
            publisherService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@Valid @RequestBody Publisher newPublisher, @PathVariable Long id) {
        Optional<Publisher> updatedPublisher = publisherService.updateById(id, newPublisher);

        if (updatedPublisher.isPresent()) {
            return new ResponseEntity<>(updatedPublisher.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
