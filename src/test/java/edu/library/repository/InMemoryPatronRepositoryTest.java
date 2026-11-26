package edu.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.library.exception.DuplicatePatronException;
import edu.library.exception.InvalidInputException;
import edu.library.exception.PatronNotFoundException;
import edu.library.model.patron.Patron;
import edu.library.policy.FacultyPolicy;
import edu.library.policy.StudentPolicy;

class InMemoryPatronRepositoryTest {

    private final PatronRepository repository = new InMemoryPatronRepository();

    // ---------- save ----------

    @Test
    void savedPatronCanBeFoundById() {
        Patron ayse = new Patron("P-1", "Ayşe", new StudentPolicy());

        repository.save(ayse);

        assertSame(ayse, repository.findById("P-1"));
    }

    @Test
    void savingSameIdTwiceThrows() {
        repository.save(new Patron("P-1", "Ayşe", new StudentPolicy()));

        DuplicatePatronException exception = assertThrows(
                DuplicatePatronException.class,
                () -> repository.save(new Patron("P-1", "Mehmet", new FacultyPolicy()))
        );

        assertEquals("Patron already exists: P-1", exception.getMessage());
    }

    @Test
    void savingNullPatronThrows() {
        assertThrows(InvalidInputException.class, () -> repository.save(null));
    }

    // ---------- findById ----------

    @Test
    void findingUnknownPatronThrows() {
        PatronNotFoundException exception = assertThrows(
                PatronNotFoundException.class,
                () -> repository.findById("P-9")
        );

        assertEquals("Patron not found: P-9", exception.getMessage());
    }

    // ---------- existsById ----------

    @Test
    void existsByIdIsTrueAfterSaving() {
        repository.save(new Patron("P-1", "Ayşe", new StudentPolicy()));

        assertTrue(repository.existsById("P-1"));
    }

    @Test
    void existsByIdIsFalseForUnknownPatron() {
        assertFalse(repository.existsById("P-9"));
    }

    // ---------- findAll ----------

    @Test
    void findAllReturnsPatronsInTheOrderTheyWereRegistered() {
        Patron ayse = new Patron("P-1", "Ayşe", new StudentPolicy());
        Patron mehmet = new Patron("P-2", "Mehmet", new FacultyPolicy());
        repository.save(ayse);
        repository.save(mehmet);

        List<Patron> patrons = repository.findAll();

        assertEquals(List.of(ayse, mehmet), patrons);
    }

    @Test
    void findAllOfEmptyRepositoryIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }
}
