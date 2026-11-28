package edu.library.repository;

import java.util.List;

import edu.library.model.loan.Loan;

/**
 * Stores every loan, open and returned.
 */
public interface LoanRepository {

    /** Adds a loan, or replaces the loan with the same id. */
    void save(Loan loan);

    /** Finds a loan by id. Throws LoanNotFoundException if there is none. */
    Loan findById(String loanId);

    /** The loans of a patron that are not returned yet. */
    List<Loan> findActiveLoansOf(String patronId);

    /** True if the item is borrowed right now and not returned yet. */
    boolean isItemOnLoan(String itemId);

    /** All loans, in the order they were made. */
    List<Loan> findAll();
}
