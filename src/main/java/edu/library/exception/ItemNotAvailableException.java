package edu.library.exception;

/**
 * Thrown when a patron tries to borrow an item that is already on loan.
 */
public class ItemNotAvailableException extends LibraryException {

    public ItemNotAvailableException(String itemId) {
        super("Item is not available: " + itemId);
    }
}
