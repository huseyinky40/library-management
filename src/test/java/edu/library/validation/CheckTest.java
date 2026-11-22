package edu.library.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.library.exception.InvalidInputException;

class CheckTest {

    // ---------- notNull ----------

    @Test
    void notNullReturnsTheValue() {
        String value = Check.notNull("hello", "Greeting");

        assertEquals("hello", value);
    }

    @Test
    void notNullWithNullThrows() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> Check.notNull(null, "Item type")
        );

        assertEquals("Item type must not be null", exception.getMessage());
    }

    // ---------- notBlank ----------

    @Test
    void notBlankRemovesOuterSpaces() {
        String text = Check.notBlank("  Dune  ", "Title");

        assertEquals("Dune", text);
    }

    @Test
    void notBlankWithNullThrows() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> Check.notBlank(null, "Title")
        );

        assertEquals("Title must not be empty", exception.getMessage());
    }

    @Test
    void notBlankWithOnlySpacesThrows() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> Check.notBlank("   ", "Title")
        );

        assertEquals("Title must not be empty", exception.getMessage());
    }
}
