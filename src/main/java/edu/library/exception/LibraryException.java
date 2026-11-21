package edu.library.exception;

/**
 * Base class of every exception in the library system.
 *
 * It is unchecked (extends RuntimeException) because these errors are broken
 * business rules, for example "the item is already borrowed". The caller cannot
 * fix them by retrying, so forcing a try/catch on every call would only add noise.
 *
 * It is abstract: code always throws one of the specific subclasses,
 * so the type of the exception already tells what went wrong.
 */
public abstract class LibraryException extends RuntimeException {

    protected LibraryException(String message) {
        super(message);
    }
}
