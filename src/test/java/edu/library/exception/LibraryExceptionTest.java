package edu.library.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Checks the exception hierarchy itself: every exception is a LibraryException
 * and builds a clear message. The tests of the library services later check
 * WHEN each exception is thrown.
 */
class LibraryExceptionTest {

    // ---------- hierarchy ----------

    @Test
    void libraryExceptionIsUnchecked() {
        LibraryException exception = new ItemNotFoundException("B-1");

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void notFoundExceptionsShareOneParent() {
        assertInstanceOf(EntityNotFoundException.class, new ItemNotFoundException("B-1"));
        assertInstanceOf(EntityNotFoundException.class, new PatronNotFoundException("P-1"));
        assertInstanceOf(EntityNotFoundException.class, new LoanNotFoundException("L-1"));
    }

    @Test
    void businessRuleExceptionsExtendLibraryException() {
        assertInstanceOf(LibraryException.class, new DuplicateItemException("B-1"));
        assertInstanceOf(LibraryException.class, new DuplicatePatronException("P-1"));
        assertInstanceOf(LibraryException.class, new ItemNotAvailableException("B-1"));
        assertInstanceOf(LibraryException.class, new BorrowLimitExceededException("P-1", 3));
        assertInstanceOf(LibraryException.class, new OutstandingFineException("P-1", BigDecimal.ONE));
        assertInstanceOf(LibraryException.class, new LoanAlreadyReturnedException("L-1"));
        assertInstanceOf(LibraryException.class, new InvalidPaymentException("bad payment"));
        assertInstanceOf(LibraryException.class, new InvalidDateException("bad date"));
        assertInstanceOf(LibraryException.class, new InvalidInputException("bad input"));
    }

    // ---------- messages ----------

    @Test
    void itemNotFoundMessageNamesTheItem() {
        LibraryException exception = new ItemNotFoundException("B-1");

        assertEquals("Item not found: B-1", exception.getMessage());
    }

    @Test
    void patronNotFoundMessageNamesThePatron() {
        LibraryException exception = new PatronNotFoundException("P-1");

        assertEquals("Patron not found: P-1", exception.getMessage());
    }

    @Test
    void loanNotFoundMessageNamesTheLoan() {
        LibraryException exception = new LoanNotFoundException("L-1");

        assertEquals("Loan not found: L-1", exception.getMessage());
    }

    @Test
    void duplicateItemMessageNamesTheItem() {
        LibraryException exception = new DuplicateItemException("B-1");

        assertEquals("Item already exists: B-1", exception.getMessage());
    }

    @Test
    void duplicatePatronMessageNamesThePatron() {
        LibraryException exception = new DuplicatePatronException("P-1");

        assertEquals("Patron already exists: P-1", exception.getMessage());
    }

    @Test
    void itemNotAvailableMessageNamesTheItem() {
        LibraryException exception = new ItemNotAvailableException("B-1");

        assertEquals("Item is not available: B-1", exception.getMessage());
    }

    @Test
    void borrowLimitMessageShowsTheLimit() {
        LibraryException exception = new BorrowLimitExceededException("P-1", 3);

        assertEquals("Patron P-1 cannot borrow more than 3 items", exception.getMessage());
    }

    @Test
    void outstandingFineMessageShowsTheAmount() {
        LibraryException exception = new OutstandingFineException("P-1", new BigDecimal("4.50"));

        assertEquals("Patron P-1 has an unpaid fine of 4.50", exception.getMessage());
    }

    @Test
    void loanAlreadyReturnedMessageNamesTheLoan() {
        LibraryException exception = new LoanAlreadyReturnedException("L-1");

        assertEquals("Loan is already returned: L-1", exception.getMessage());
    }

    @Test
    void invalidPaymentKeepsTheGivenMessage() {
        LibraryException exception = new InvalidPaymentException("Amount must be positive");

        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void invalidDateKeepsTheGivenMessage() {
        LibraryException exception = new InvalidDateException("Return date is before borrow date");

        assertEquals("Return date is before borrow date", exception.getMessage());
    }

    @Test
    void invalidInputKeepsTheGivenMessage() {
        LibraryException exception = new InvalidInputException("Title must not be empty");

        assertEquals("Title must not be empty", exception.getMessage());
    }
}
