package edu.library.validation;

import edu.library.exception.InvalidInputException;

/**
 * Small helper for checking constructor and method arguments.
 *
 * Every class in the library uses these checks, so the same mistake
 * (for example an empty title) always gives the same kind of error.
 */
public final class Check {

    private Check() {
        // only static methods, no objects needed
    }

    /**
     * Makes sure a value is not null.
     *
     * @return the same value, so it can be assigned in one line
     */
    public static <T> T notNull(T value, String fieldName) {
        if (value == null) {
            throw new InvalidInputException(fieldName + " must not be null");
        }
        return value;
    }

    /**
     * Makes sure a text is not null, not empty and not only spaces.
     *
     * @return the text without spaces at the start and the end
     */
    public static String notBlank(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new InvalidInputException(fieldName + " must not be empty");
        }
        return text.strip();
    }
}
