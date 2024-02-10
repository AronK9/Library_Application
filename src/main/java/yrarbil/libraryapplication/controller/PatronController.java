package yrarbil.libraryapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yrarbil.libraryapplication.model.Patron;
import yrarbil.libraryapplication.service.PatronService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patron")
public class PatronController {

    private final PatronService patronService;

    @Autowired
    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        List<Patron> patrons = patronService.getAllPatrons();

        return new ResponseEntity<>(patrons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById (@PathVariable Long id) {

        Optional<Patron> patron = patronService.getPatronById(id);

        if (patron.isPresent()) {
            return new ResponseEntity<>(patron.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Patron> addPatron(@RequestBody Patron patron) {

        Patron createdPatron = patronService.save(patron);
        return new ResponseEntity<>(createdPatron, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron (@PathVariable Long id) {
        patronService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron (@RequestBody Patron newPatron, @PathVariable Long id) {
        Optional<Patron> updatedPatron = patronService.updateById(id, newPatron);

        if (updatedPatron.isPresent()) {
            return new ResponseEntity<>(updatedPatron.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
