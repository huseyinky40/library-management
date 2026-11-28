package edu.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.library.exception.InvalidInputException;
import edu.library.exception.LoanNotFoundException;
import edu.library.model.item.Book;
import edu.library.model.item.Dvd;
import edu.library.model.item.LibraryItem;
import edu.library.model.loan.Loan;
import edu.library.model.patron.Patron;
import edu.library.policy.FacultyPolicy;
import edu.library.policy.StudentPolicy;

class InMemoryLoanRepositoryTest {

    private static final LocalDate BORROW_DATE = LocalDate.of(2026, 11, 1);

    private final LoanRepository repository = new InMemoryLoanRepository();

    private final LibraryItem book = new Book("B-1", "Dune");
    private final LibraryItem dvd = new Dvd("D-1", "Inception");
    private final Patron ayse = new Patron("P-1", "Ayşe", new StudentPolicy());
    private final Patron mehmet = new Patron("P-2", "Mehmet", new FacultyPolicy());

    // ---------- save and findById ----------

    @Test
    void savedLoanCanBeFoundById() {
        Loan loan = new Loan("L-1", book, ayse, BORROW_DATE);

        repository.save(loan);

        assertSame(loan, repository.findById("L-1"));
    }

    @Test
    void findingUnknownLoanThrows() {
        LoanNotFoundException exception = assertThrows(
                LoanNotFoundException.class,
                () -> repository.findById("L-9")
        );

        assertEquals("Loan not found: L-9", exception.getMessage());
    }

    @Test
    void savingNullLoanThrows() {
        assertThrows(InvalidInputException.class, () -> repository.save(null));
    }

    // ---------- findActiveLoansOf ----------

    @Test
    void activeLoansOfPatronIncludeOnlyTheirOpenLoans() {
        Loan aysesBook = new Loan("L-1", book, ayse, BORROW_DATE);
        Loan aysesDvd = new Loan("L-2", dvd, ayse, BORROW_DATE);
        aysesDvd.markReturned(BORROW_DATE.plusDays(3));
        repository.save(aysesBook);
        repository.save(aysesDvd);

        List<Loan> activeLoans = repository.findActiveLoansOf("P-1");

        assertEquals(List.of(aysesBook), activeLoans);
    }

    @Test
    void activeLoansOfPatronDoNotIncludeOtherPatrons() {
        repository.save(new Loan("L-1", book, mehmet, BORROW_DATE));

        assertTrue(repository.findActiveLoansOf("P-1").isEmpty());
    }

    // ---------- isItemOnLoan ----------

    @Test
    void itemIsOnLoanWhileLoanIsOpen() {
        repository.save(new Loan("L-1", book, ayse, BORROW_DATE));

        assertTrue(repository.isItemOnLoan("B-1"));
    }

    @Test
    void itemIsNotOnLoanAfterItIsReturned() {
        Loan loan = new Loan("L-1", book, ayse, BORROW_DATE);
        loan.markReturned(BORROW_DATE.plusDays(5));
        repository.save(loan);

        assertFalse(repository.isItemOnLoan("B-1"));
    }

    @Test
    void itemNeverBorrowedIsNotOnLoan() {
        assertFalse(repository.isItemOnLoan("B-1"));
    }

    // ---------- findAll ----------

    @Test
    void findAllReturnsOpenAndReturnedLoans() {
        Loan open = new Loan("L-1", book, ayse, BORROW_DATE);
        Loan returned = new Loan("L-2", dvd, ayse, BORROW_DATE);
        returned.markReturned(BORROW_DATE.plusDays(1));
        repository.save(open);
        repository.save(returned);

        assertEquals(List.of(open, returned), repository.findAll());
    }
}
