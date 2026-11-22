package edu.library.model.item;

/**
 * A magazine. It can be borrowed for 14 days.
 */
public class Magazine extends LibraryItem {

    private static final int LOAN_PERIOD_DAYS = 14;

    public Magazine(String id, String title) {
        super(id, title);
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_PERIOD_DAYS;
    }

    @Override
    public ItemType getType() {
        return ItemType.MAGAZINE;
    }
}
