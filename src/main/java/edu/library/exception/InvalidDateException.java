package edu.library.exception;

/**
 * Thrown when a date does not make sense, for example
 * returning an item before the day it was borrowed.
 * The message explains the exact reason.
 */
public class InvalidDateException extends LibraryException {

    public InvalidDateException(String message) {
        super(message);
    }
}
