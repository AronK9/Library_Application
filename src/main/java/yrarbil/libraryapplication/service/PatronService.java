package yrarbil.libraryapplication.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yrarbil.libraryapplication.model.Patron;
import yrarbil.libraryapplication.repository.PatronRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    private final PatronRepository patronRepository;

    @Autowired
    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Optional<Patron> getPatronById(Long id) {
        return patronRepository.findById(id);
    }

    @Transactional
    public Patron save(Patron patron) {
        if (patron == null)
            throw new IllegalArgumentException("Patron cannot be null");
        return patronRepository.save(patron);
    }

    public void delete(Long id) {
        if(patronRepository.existsById(id)) {
            patronRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Patron with id " + id + " does not exist!");
        }
    }

    public Optional <Patron> updateById (Long id, Patron updatedPatron) {

        Optional <Patron> optionalPatron = patronRepository.findById(id);

        if(optionalPatron.isPresent()) {
            Patron patron = optionalPatron.get();

            patron.setUsername(updatedPatron.getUsername());
            patron.setName(updatedPatron.getName());
            patron.setAge(updatedPatron.getAge());
            patron.setLoanHistory(updatedPatron.getLoanHistory());

            patron = patronRepository.save(patron);
            return Optional.of(patron);
        }
        return Optional.empty();
    }
}
