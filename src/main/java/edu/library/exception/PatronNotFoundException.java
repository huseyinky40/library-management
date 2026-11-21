package edu.library.exception;

/**
 * Thrown when no patron with the given id exists in the library.
 */
public class PatronNotFoundException extends EntityNotFoundException {

    public PatronNotFoundException(String patronId) {
        super("Patron", patronId);
    }
}
