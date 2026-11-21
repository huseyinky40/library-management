package edu.library.exception;

/**
 * Groups the "we looked for something by its id and it is not there" errors.
 * A caller can catch this one type to handle every not-found case together.
 */
public abstract class EntityNotFoundException extends LibraryException {

    protected EntityNotFoundException(String entityName, String id) {
        super(entityName + " not found: " + id);
    }
}
