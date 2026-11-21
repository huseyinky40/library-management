package edu.library.exception;

/**
 * Thrown when an object is created with bad data,
 * for example an empty title or a missing id.
 * The message explains the exact reason.
 */
public class InvalidInputException extends LibraryException {

    public InvalidInputException(String message) {
        super(message);
    }
}
