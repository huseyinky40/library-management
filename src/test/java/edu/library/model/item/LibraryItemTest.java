package edu.library.model.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import edu.library.exception.InvalidDateException;
import edu.library.exception.InvalidInputException;

class LibraryItemTest {

    private static final LocalDate BORROW_DATE = LocalDate.of(2026, 11, 1);

    // ---------- loan period depends on the item type ----------

    @Test
    void bookCanBeBorrowedFor21Days() {
        LibraryItem book = new Book("B-1", "Dune");

        assertEquals(21, book.getLoanPeriodDays());
    }

    @Test
    void magazineCanBeBorrowedFor14Days() {
        LibraryItem magazine = new Magazine("M-1", "National Geographic");

        assertEquals(14, magazine.getLoanPeriodDays());
    }

    @Test
    void dvdCanBeBorrowedFor7Days() {
        LibraryItem dvd = new Dvd("D-1", "Inception");

        assertEquals(7, dvd.getLoanPeriodDays());
    }

    // ---------- calculateDueDate ----------

    @Test
    void bookDueDateIs21DaysAfterBorrowing() {
        LibraryItem book = new Book("B-1", "Dune");

        LocalDate dueDate = book.calculateDueDate(BORROW_DATE);

        assertEquals(LocalDate.of(2026, 11, 22), dueDate);
    }

    @Test
    void magazineDueDateIs14DaysAfterBorrowing() {
        LibraryItem magazine = new Magazine("M-1", "National Geographic");

        LocalDate dueDate = magazine.calculateDueDate(BORROW_DATE);

        assertEquals(LocalDate.of(2026, 11, 15), dueDate);
    }

    @Test
    void dvdDueDateIs7DaysAfterBorrowing() {
        LibraryItem dvd = new Dvd("D-1", "Inception");

        LocalDate dueDate = dvd.calculateDueDate(BORROW_DATE);

        assertEquals(LocalDate.of(2026, 11, 8), dueDate);
    }

    @Test
    void dueDateCanFallInTheNextMonth() {
        LibraryItem book = new Book("B-1", "Dune");

        LocalDate dueDate = book.calculateDueDate(LocalDate.of(2026, 12, 20));

        assertEquals(LocalDate.of(2027, 1, 10), dueDate);
    }

    @Test
    void dueDateWithoutBorrowDateThrows() {
        LibraryItem book = new Book("B-1", "Dune");

        InvalidDateException exception = assertThrows(
                InvalidDateException.class,
                () -> book.calculateDueDate(null)
        );

        assertEquals("Borrow date must not be null", exception.getMessage());
    }

    // ---------- creating an item ----------

    @Test
    void newItemKeepsIdAndTitle() {
        LibraryItem book = new Book("B-1", "Dune");

        assertEquals("B-1", book.getId());
        assertEquals("Dune", book.getTitle());
    }

    @Test
    void itemWithEmptyTitleCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new Book("B-1", "")
        );

        assertEquals("Title must not be empty", exception.getMessage());
    }

    @Test
    void itemWithoutIdCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new Dvd(null, "Inception")
        );

        assertEquals("Item id must not be empty", exception.getMessage());
    }

    // ---------- type ----------

    @Test
    void eachItemReportsItsOwnType() {
        assertEquals(ItemType.BOOK, new Book("B-1", "Dune").getType());
        assertEquals(ItemType.MAGAZINE, new Magazine("M-1", "Time").getType());
        assertEquals(ItemType.DVD, new Dvd("D-1", "Inception").getType());
    }

    // ---------- equals ----------

    @Test
    void itemsWithSameIdAreEqual() {
        LibraryItem first = new Book("B-1", "Dune");
        LibraryItem second = new Book("B-1", "Dune (2nd edition)");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void itemsWithDifferentIdsAreNotEqual() {
        LibraryItem first = new Book("B-1", "Dune");
        LibraryItem second = new Book("B-2", "Dune");

        assertNotEquals(first, second);
    }

    @Test
    void toStringShowsTypeIdAndTitle() {
        LibraryItem book = new Book("B-1", "Dune");

        assertEquals("BOOK(B-1, Dune)", book.toString());
    }
}
