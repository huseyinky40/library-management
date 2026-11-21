package edu.library.exception;

/**
 * Thrown when a patron already has as many items as their type allows.
 */
public class BorrowLimitExceededException extends LibraryException {

    public BorrowLimitExceededException(String patronId, int limit) {
        super("Patron " + patronId + " cannot borrow more than " + limit + " items");
    }
}
