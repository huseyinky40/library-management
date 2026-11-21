package edu.library.exception;

/**
 * Thrown when we try to return a loan that was already returned.
 */
public class LoanAlreadyReturnedException extends LibraryException {

    public LoanAlreadyReturnedException(String loanId) {
        super("Loan is already returned: " + loanId);
    }
}
