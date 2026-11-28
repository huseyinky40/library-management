package edu.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.library.exception.BorrowLimitExceededException;
import edu.library.exception.InvalidDateException;
import edu.library.exception.InvalidInputException;
import edu.library.exception.ItemNotAvailableException;
import edu.library.exception.ItemNotFoundException;
import edu.library.exception.LoanAlreadyReturnedException;
import edu.library.exception.LoanNotFoundException;
import edu.library.exception.OutstandingFineException;
import edu.library.exception.PatronNotFoundException;
import edu.library.model.item.Book;
import edu.library.model.item.Dvd;
import edu.library.model.item.Magazine;
import edu.library.model.loan.Loan;
import edu.library.model.patron.Patron;
import edu.library.policy.FacultyPolicy;
import edu.library.policy.StudentPolicy;
import edu.library.repository.InMemoryItemRepository;
import edu.library.repository.InMemoryLoanRepository;
import edu.library.repository.InMemoryPatronRepository;
import edu.library.repository.ItemRepository;
import edu.library.repository.LoanRepository;
import edu.library.repository.PatronRepository;

class LendingServiceTest {

    private static final LocalDate NOV_1 = LocalDate.of(2026, 11, 1);
    private static final LocalDate NOV_10 = LocalDate.of(2026, 11, 10);

    private ItemRepository items;
    private PatronRepository patrons;
    private LoanRepository loans;
    private LendingService service;

    private Patron student;

    /** Runs before every test, so each test starts with a fresh library. */
    @BeforeEach
    void setUp() {
        items = new InMemoryItemRepository();
        patrons = new InMemoryPatronRepository();
        loans = new InMemoryLoanRepository();
        service = new LendingService(items, patrons, loans);

        items.save(new Book("B-1", "Dune"));
        items.save(new Book("B-2", "Emma"));
        items.save(new Magazine("M-1", "Time"));
        items.save(new Dvd("D-1", "Inception"));

        student = new Patron("P-1", "Ayşe", new StudentPolicy());
        patrons.save(student);
        patrons.save(new Patron("P-2", "Mehmet", new FacultyPolicy()));
    }

    // ---------- borrow: success ----------

    @Test
    void borrowCreatesALoanWithTheRightDueDate() {
        Loan loan = service.borrow("P-1", "B-1", NOV_1);

        assertEquals("L-1", loan.getId());
        assertEquals("B-1", loan.getItem().getId());
        assertEquals("P-1", loan.getPatron().getId());
        assertEquals(LocalDate.of(2026, 11, 22), loan.getDueDate());
    }

    @Test
    void borrowSavesTheLoan() {
        Loan loan = service.borrow("P-1", "B-1", NOV_1);

        assertEquals(loan, loans.findById("L-1"));
        assertTrue(loans.isItemOnLoan("B-1"));
    }

    @Test
    void eachLoanGetsTheNextId() {
        Loan first = service.borrow("P-1", "B-1", NOV_1);
        Loan second = service.borrow("P-1", "D-1", NOV_1);

        assertEquals("L-1", first.getId());
        assertEquals("L-2", second.getId());
    }

    @Test
    void facultyCanBorrowMoreThanThreeItems() {
        service.borrow("P-2", "B-1", NOV_1);
        service.borrow("P-2", "B-2", NOV_1);
        service.borrow("P-2", "M-1", NOV_1);

        Loan fourth = service.borrow("P-2", "D-1", NOV_1);

        assertEquals("P-2", fourth.getPatron().getId());
    }

    @Test
    void returnedItemCanBeBorrowedAgain() {
        Loan first = service.borrow("P-1", "B-1", NOV_1);
        service.returnItem(first.getId(), NOV_10);

        Loan second = service.borrow("P-2", "B-1", NOV_10);

        assertEquals("P-2", second.getPatron().getId());
    }

    // ---------- borrow: failures ----------

    @Test
    void borrowingUnknownItemThrows() {
        assertThrows(ItemNotFoundException.class, () -> service.borrow("P-1", "X-9", NOV_1));
    }

    @Test
    void borrowingForUnknownPatronThrows() {
        assertThrows(PatronNotFoundException.class, () -> service.borrow("P-9", "B-1", NOV_1));
    }

    @Test
    void borrowingWithoutDateThrows() {
        InvalidDateException exception = assertThrows(
                InvalidDateException.class,
                () -> service.borrow("P-1", "B-1", null)
        );

        assertEquals("Borrow date must not be null", exception.getMessage());
    }

    @Test
    void borrowingItemThatIsOnLoanThrows() {
        service.borrow("P-1", "B-1", NOV_1);

        ItemNotAvailableException exception = assertThrows(
                ItemNotAvailableException.class,
                () -> service.borrow("P-2", "B-1", NOV_1)
        );

        assertEquals("Item is not available: B-1", exception.getMessage());
    }

    @Test
    void studentCannotBorrowFourthItem() {
        service.borrow("P-1", "B-1", NOV_1);
        service.borrow("P-1", "B-2", NOV_1);
        service.borrow("P-1", "M-1", NOV_1);

        BorrowLimitExceededException exception = assertThrows(
                BorrowLimitExceededException.class,
                () -> service.borrow("P-1", "D-1", NOV_1)
        );

        assertEquals("Patron P-1 cannot borrow more than 3 items", exception.getMessage());
    }

    @Test
    void patronWithUnpaidFineCannotBorrow() {
        student.addFine(new BigDecimal("2.00"));

        OutstandingFineException exception = assertThrows(
                OutstandingFineException.class,
                () -> service.borrow("P-1", "B-1", NOV_1)
        );

        assertEquals("Patron P-1 has an unpaid fine of 2.00", exception.getMessage());
    }

    @Test
    void failedBorrowDoesNotCreateALoan() {
        student.addFine(new BigDecimal("2.00"));

        assertThrows(OutstandingFineException.class, () -> service.borrow("P-1", "B-1", NOV_1));

        assertTrue(loans.findAll().isEmpty());
    }

    // ---------- returnItem ----------

    @Test
    void returnItemClosesTheLoan() {
        Loan loan = service.borrow("P-1", "B-1", NOV_1);

        Loan returned = service.returnItem(loan.getId(), NOV_10);

        assertTrue(returned.isReturned());
        assertFalse(loans.isItemOnLoan("B-1"));
    }

    @Test
    void returningFreesASlotForTheStudent() {
        Loan first = service.borrow("P-1", "B-1", NOV_1);
        service.borrow("P-1", "B-2", NOV_1);
        service.borrow("P-1", "M-1", NOV_1);
        service.returnItem(first.getId(), NOV_10);

        Loan fourth = service.borrow("P-1", "D-1", NOV_10);

        assertEquals("D-1", fourth.getItem().getId());
    }

    @Test
    void returningUnknownLoanThrows() {
        assertThrows(LoanNotFoundException.class, () -> service.returnItem("L-9", NOV_10));
    }

    @Test
    void returningSameLoanTwiceThrows() {
        Loan loan = service.borrow("P-1", "B-1", NOV_1);
        service.returnItem(loan.getId(), NOV_10);

        assertThrows(LoanAlreadyReturnedException.class, () -> service.returnItem(loan.getId(), NOV_10));
    }

    @Test
    void returningBeforeBorrowDateThrows() {
        Loan loan = service.borrow("P-1", "B-1", NOV_10);

        assertThrows(InvalidDateException.class, () -> service.returnItem(loan.getId(), NOV_1));
    }

    // ---------- constructor ----------

    @Test
    void serviceWithoutRepositoryCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new LendingService(items, patrons, null)
        );

        assertEquals("Loan repository must not be null", exception.getMessage());
    }
}
