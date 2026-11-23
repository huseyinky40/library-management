package edu.library.policy;

import java.math.BigDecimal;

import edu.library.model.patron.PatronType;

/**
 * The rules that depend on the patron type.
 *
 * Strategy pattern: a Patron holds one PatronPolicy and asks it for the rules.
 * Students and faculty members get different policy objects,
 * but the Patron class is the same for both.
 */
public interface PatronPolicy {

    /** Which patron type these rules belong to. */
    PatronType getType();

    /** How many items a patron can have on loan at the same time. */
    int getBorrowingLimit();

    /** How much the patron pays for each late day. */
    BigDecimal getDailyFine();
}
