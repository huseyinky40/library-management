package edu.library.exception;

/**
 * Thrown when no loan with the given id exists in the library.
 */
public class LoanNotFoundException extends EntityNotFoundException {

    public LoanNotFoundException(String loanId) {
        super("Loan", loanId);
    }
}
