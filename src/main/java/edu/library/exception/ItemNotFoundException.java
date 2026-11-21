package edu.library.exception;

/**
 * Thrown when no item with the given id exists in the library.
 */
public class ItemNotFoundException extends EntityNotFoundException {

    public ItemNotFoundException(String itemId) {
        super("Item", itemId);
    }
}
