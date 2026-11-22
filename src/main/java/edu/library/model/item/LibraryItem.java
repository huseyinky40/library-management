package edu.library.model.item;

import java.time.LocalDate;
import java.util.Objects;

import edu.library.exception.InvalidDateException;
import edu.library.validation.Check;

/**
 * Something the library lends: a book, a magazine or a DVD.
 *
 * Each subclass says how many days it can be borrowed (getLoanPeriodDays).
 * The due date calculation is written once here and works for every subclass.
 * Adding a new item type means writing a new subclass; this class does not change.
 *
 * An item never changes after it is created. Whether it is on loan or not
 * is decided by the loans, not stored in the item.
 */
public abstract class LibraryItem {

    private final String id;
    private final String title;

    protected LibraryItem(String id, String title) {
        this.id = Check.notBlank(id, "Item id");
        this.title = Check.notBlank(title, "Title");
    }

    /** How many days a patron can keep this kind of item. */
    public abstract int getLoanPeriodDays();

    /** Which kind of item this is. */
    public abstract ItemType getType();

    /**
     * The last day the item can be returned without a fine.
     *
     * @param borrowDate the day the item is borrowed
     */
    public LocalDate calculateDueDate(LocalDate borrowDate) {
        if (borrowDate == null) {
            throw new InvalidDateException("Borrow date must not be null");
        }
        return borrowDate.plusDays(getLoanPeriodDays());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    /** Two items are the same item when they have the same id. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        LibraryItem item = (LibraryItem) other;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getType() + "(" + id + ", " + title + ")";
    }
}
