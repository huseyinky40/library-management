package edu.library.exception;

/**
 * Thrown when a fine payment is not allowed, for example a negative amount
 * or more money than the patron owes.
 * The message explains the exact reason.
 */
public class InvalidPaymentException extends LibraryException {

    public InvalidPaymentException(String message) {
        super(message);
    }
}
