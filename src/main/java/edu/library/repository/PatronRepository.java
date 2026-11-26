package edu.library.repository;

import java.util.List;

import edu.library.model.patron.Patron;

/**
 * Stores the patrons (members) of the library.
 *
 * Services only know this interface, not how the patrons are stored.
 */
public interface PatronRepository {

    /** Adds a new patron. Throws DuplicatePatronException if the id is already used. */
    void save(Patron patron);

    /** Finds a patron by id. Throws PatronNotFoundException if there is none. */
    Patron findById(String patronId);

    boolean existsById(String patronId);

    /** All patrons, in the order they were registered. */
    List<Patron> findAll();
}
