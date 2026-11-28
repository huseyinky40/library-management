package edu.library.model.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import edu.library.exception.InvalidDateException;
import edu.library.exception.InvalidInputException;
import edu.library.exception.LoanAlreadyReturnedException;
import edu.library.model.item.Book;
import edu.library.model.item.Dvd;
import edu.library.model.item.LibraryItem;
import edu.library.model.patron.Patron;
import edu.library.policy.StudentPolicy;

class LoanTest {

    private static final LocalDate BORROW_DATE = LocalDate.of(2026, 11, 1);

    private final LibraryItem book = new Book("B-1", "Dune");
    private final Patron ayse = new Patron("P-1", "Ayşe", new StudentPolicy());

    private Loan newLoan() {
        return new Loan("L-1", book, ayse, BORROW_DATE);
    }

    // ---------- creating a loan ----------

    @Test
    void newLoanKeepsItsDetails() {
        Loan loan = newLoan();

        assertEquals("L-1", loan.getId());
        assertEquals(book, loan.getItem());
        assertEquals(ayse, loan.getPatron());
        assertEquals(BORROW_DATE, loan.getBorrowDate());
    }

    @Test
    void dueDateComesFromTheItemType() {
        Loan bookLoan = newLoan();
        Loan dvdLoan = new Loan("L-2", new Dvd("D-1", "Inception"), ayse, BORROW_DATE);

        assertEquals(LocalDate.of(2026, 11, 22), bookLoan.getDueDate());
        assertEquals(LocalDate.of(2026, 11, 8), dvdLoan.getDueDate());
    }

    @Test
    void newLoanIsNotReturned() {
        Loan loan = newLoan();

        assertFalse(loan.isReturned());
        assertEquals(Optional.empty(), loan.getReturnDate());
    }

    @Test
    void loanWithoutBorrowDateCannotBeCreated() {
        assertThrows(InvalidDateException.class, () -> new Loan("L-1", book, ayse, null));
    }

    @Test
    void loanWithoutItemCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new Loan("L-1", null, ayse, BORROW_DATE)
        );

        assertEquals("Item must not be null", exception.getMessage());
    }

    @Test
    void loanWithoutPatronCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new Loan("L-1", book, null, BORROW_DATE)
        );

        assertEquals("Patron must not be null", exception.getMessage());
    }

    // ---------- markReturned ----------

    @Test
    void markReturnedClosesTheLoan() {
        Loan loan = newLoan();

        loan.markReturned(LocalDate.of(2026, 11, 10));

        assertTrue(loan.isReturned());
        assertEquals(Optional.of(LocalDate.of(2026, 11, 10)), loan.getReturnDate());
    }

    @Test
    void itemCanBeReturnedOnTheSameDay() {
        Loan loan = newLoan();

        loan.markReturned(BORROW_DATE);

        assertTrue(loan.isReturned());
    }

    @Test
    void returningTwiceThrows() {
        Loan loan = newLoan();
        loan.markReturned(LocalDate.of(2026, 11, 10));

        LoanAlreadyReturnedException exception = assertThrows(
                LoanAlreadyReturnedException.class,
                () -> loan.markReturned(LocalDate.of(2026, 11, 12))
        );

        assertEquals("Loan is already returned: L-1", exception.getMessage());
    }

    @Test
    void returningBeforeBorrowDateThrows() {
        Loan loan = newLoan();

        InvalidDateException exception = assertThrows(
                InvalidDateException.class,
                () -> loan.markReturned(LocalDate.of(2026, 10, 31))
        );

        assertEquals("Return date 2026-10-31 is before borrow date 2026-11-01", exception.getMessage());
    }

    @Test
    void returningWithoutDateThrows() {
        Loan loan = newLoan();

        assertThrows(InvalidDateException.class, () -> loan.markReturned(null));
    }

    // ---------- equals and toString ----------

    @Test
    void loansWithSameIdAreEqual() {
        Loan first = newLoan();
        Loan second = new Loan("L-1", book, ayse, LocalDate.of(2026, 11, 5));

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void loansWithDifferentIdsAreNotEqual() {
        Loan first = newLoan();
        Loan second = new Loan("L-2", book, ayse, BORROW_DATE);

        assertNotEquals(first, second);
    }

    @Test
    void toStringShowsItemPatronAndDueDate() {
        assertEquals("Loan(L-1, B-1 -> P-1, due 2026-11-22)", newLoan().toString());
    }
}
