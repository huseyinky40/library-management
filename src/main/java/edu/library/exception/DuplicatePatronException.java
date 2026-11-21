package edu.library.exception;

/**
 * Thrown when we try to add a patron whose id is already used.
 */
public class DuplicatePatronException extends LibraryException {

    public DuplicatePatronException(String patronId) {
        super("Patron already exists: " + patronId);
    }
}
