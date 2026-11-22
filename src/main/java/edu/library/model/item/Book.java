package edu.library.model.item;

/**
 * A book. It can be borrowed for 21 days.
 */
public class Book extends LibraryItem {

    private static final int LOAN_PERIOD_DAYS = 21;

    public Book(String id, String title) {
        super(id, title);
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_PERIOD_DAYS;
    }

    @Override
    public ItemType getType() {
        return ItemType.BOOK;
    }
}
