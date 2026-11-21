package edu.library.exception;

/**
 * Thrown when we try to add a item whose id is already used.
 */
public class DuplicateItemException extends LibraryException {

    public DuplicateItemException(String itemId) {
        super("Item already exists: " + itemId);
    }
}
