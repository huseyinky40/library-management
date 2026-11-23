package edu.library.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.library.exception.InvalidInputException;
import edu.library.model.patron.Patron;
import edu.library.model.patron.PatronType;

class PatronFactoryTest {

    private final PatronFactory factory = new PatronFactory();

    @Test
    void createsStudentWithStudentRules() {
        Patron patron = factory.create(PatronType.STUDENT, "P-1", "Ayşe");

        assertEquals(PatronType.STUDENT, patron.getType());
        assertEquals(3, patron.getBorrowingLimit());
    }

    @Test
    void createsFacultyWithFacultyRules() {
        Patron patron = factory.create(PatronType.FACULTY, "P-2", "Mehmet");

        assertEquals(PatronType.FACULTY, patron.getType());
        assertEquals(10, patron.getBorrowingLimit());
    }

    @Test
    void createdPatronKeepsIdAndName() {
        Patron patron = factory.create(PatronType.STUDENT, "P-1", "Ayşe");

        assertEquals("P-1", patron.getId());
        assertEquals("Ayşe", patron.getName());
    }

    @Test
    void creatingWithoutTypeThrows() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> factory.create(null, "P-1", "Ayşe")
        );

        assertEquals("Patron type must not be null", exception.getMessage());
    }

    @Test
    void creatingWithEmptyNameThrows() {
        assertThrows(
                InvalidInputException.class,
                () -> factory.create(PatronType.FACULTY, "P-2", "")
        );
    }
}
