package edu.library.model.item;

/**
 * A DVD. It can be borrowed for 7 days, the shortest period,
 * because DVDs are popular and the library has few copies.
 */
public class Dvd extends LibraryItem {

    private static final int LOAN_PERIOD_DAYS = 7;

    public Dvd(String id, String title) {
        super(id, title);
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_PERIOD_DAYS;
    }

    @Override
    public ItemType getType() {
        return ItemType.DVD;
    }
}
