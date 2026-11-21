package edu.library.exception;

import java.math.BigDecimal;

/**
 * Thrown when a patron with an unpaid fine tries to borrow an item.
 */
public class OutstandingFineException extends LibraryException {

    public OutstandingFineException(String patronId, BigDecimal unpaidFine) {
        super("Patron " + patronId + " has an unpaid fine of " + unpaidFine.toPlainString());
    }
}
