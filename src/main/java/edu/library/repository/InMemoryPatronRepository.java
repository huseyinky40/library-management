package edu.library.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.library.exception.DuplicatePatronException;
import edu.library.exception.PatronNotFoundException;
import edu.library.model.patron.Patron;
import edu.library.validation.Check;

/**
 * Keeps the patrons in a Map in memory: id -> patron.
 * LinkedHashMap remembers the order the patrons were registered.
 */
public class InMemoryPatronRepository implements PatronRepository {

    private final Map<String, Patron> patronsById = new LinkedHashMap<>();

    @Override
    public void save(Patron patron) {
        Check.notNull(patron, "Patron");

        if (existsById(patron.getId())) {
            throw new DuplicatePatronException(patron.getId());
        }
        patronsById.put(patron.getId(), patron);
    }

    @Override
    public Patron findById(String patronId) {
        Patron patron = patronsById.get(patronId);

        if (patron == null) {
            throw new PatronNotFoundException(patronId);
        }
        return patron;
    }

    @Override
    public boolean existsById(String patronId) {
        return patronsById.containsKey(patronId);
    }

    @Override
    public List<Patron> findAll() {
        return List.copyOf(patronsById.values());   // a copy, so callers cannot change our map
    }
}
