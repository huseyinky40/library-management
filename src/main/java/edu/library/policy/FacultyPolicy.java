package edu.library.policy;

import java.math.BigDecimal;

import edu.library.model.patron.PatronType;

/**
 * Rules for faculty members: up to 10 items, 0.50 fine for each late day.
 * Faculty members need more items for teaching and research, so the limit is higher.
 */
public class FacultyPolicy implements PatronPolicy {

    private static final int BORROWING_LIMIT = 10;
    private static final BigDecimal DAILY_FINE = new BigDecimal("0.50");

    @Override
    public PatronType getType() {
        return PatronType.FACULTY;
    }

    @Override
    public int getBorrowingLimit() {
        return BORROWING_LIMIT;
    }

    @Override
    public BigDecimal getDailyFine() {
        return DAILY_FINE;
    }
}
