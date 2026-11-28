package edu.library.model.loan;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import edu.library.exception.InvalidDateException;
import edu.library.exception.LoanAlreadyReturnedException;
import edu.library.model.item.LibraryItem;
import edu.library.model.patron.Patron;
import edu.library.validation.Check;

/**
 * One borrowing: which patron took which item, when, and when it must come back.
 *
 * The due date is calculated by the item, because the loan period
 * depends on the item type (book, magazine, DVD).
 */
public class Loan {

    private final String id;
    private final LibraryItem item;
    private final Patron patron;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;   // stays empty until the item is returned

    public Loan(String id, LibraryItem item, Patron patron, LocalDate borrowDate) {
        this.id = Check.notBlank(id, "Loan id");
        this.item = Check.notNull(item, "Item");
        this.patron = Check.notNull(patron, "Patron");
        this.dueDate = item.calculateDueDate(borrowDate);   // also checks borrowDate is not null
        this.borrowDate = borrowDate;
    }

    /**
     * Closes the loan.
     *
     * @param date the day the item is brought back
     */
    public void markReturned(LocalDate date) {
        if (isReturned()) {
            throw new LoanAlreadyReturnedException(id);
        }
        if (date == null) {
            throw new InvalidDateException("Return date must not be null");
        }
        if (date.isBefore(borrowDate)) {
            throw new InvalidDateException(
                    "Return date " + date + " is before borrow date " + borrowDate);
        }
        returnDate = date;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    // ---------- getters ----------

    public String getId() {
        return id;
    }

    public LibraryItem getItem() {
        return item;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /** The return date, or an empty Optional if the item is still on loan. */
    public Optional<LocalDate> getReturnDate() {
        return Optional.ofNullable(returnDate);
    }

    // ---------- equals, hashCode, toString ----------

    /** Two loans are the same loan when they have the same id. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Loan loan = (Loan) other;
        return id.equals(loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Loan(" + id + ", " + item.getId() + " -> " + patron.getId() + ", due " + dueDate + ")";
    }
}
