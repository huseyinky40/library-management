package edu.library.policy;

import java.math.BigDecimal;

import edu.library.model.patron.PatronType;

/**
 * Rules for students: up to 3 items, 1.00 fine for each late day.
 */
public class StudentPolicy implements PatronPolicy {

    private static final int BORROWING_LIMIT = 3;
    private static final BigDecimal DAILY_FINE = new BigDecimal("1.00");

    @Override
    public PatronType getType() {
        return PatronType.STUDENT;
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
