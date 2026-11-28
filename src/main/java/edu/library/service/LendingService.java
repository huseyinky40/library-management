package edu.library.service;

import java.time.LocalDate;

import edu.library.exception.BorrowLimitExceededException;
import edu.library.exception.InvalidDateException;
import edu.library.exception.ItemNotAvailableException;
import edu.library.exception.OutstandingFineException;
import edu.library.model.item.LibraryItem;
import edu.library.model.loan.Loan;
import edu.library.model.patron.Patron;
import edu.library.repository.ItemRepository;
import edu.library.repository.LoanRepository;
import edu.library.repository.PatronRepository;
import edu.library.validation.Check;

/**
 * Borrowing and returning items.
 *
 * The repositories come in through the constructor (dependency injection).
 * This class only knows the repository interfaces, so tests can give it
 * in-memory repositories.
 */
public class LendingService {

    private final ItemRepository items;
    private final PatronRepository patrons;
    private final LoanRepository loans;
    private int nextLoanNumber = 1;

    public LendingService(ItemRepository items, PatronRepository patrons, LoanRepository loans) {
        this.items = Check.notNull(items, "Item repository");
        this.patrons = Check.notNull(patrons, "Patron repository");
        this.loans = Check.notNull(loans, "Loan repository");
    }

    /**
     * Lends an item to a patron.
     *
     * The patron cannot borrow when:
     * - the item is already on loan,
     * - the patron has an unpaid fine,
     * - the patron already has as many items as their type allows.
     *
     * @return the new loan
     */
    public Loan borrow(String patronId, String itemId, LocalDate borrowDate) {
        Patron patron = patrons.findById(patronId);
        LibraryItem item = items.findById(itemId);

        if (borrowDate == null) {
            throw new InvalidDateException("Borrow date must not be null");
        }
        if (loans.isItemOnLoan(itemId)) {
            throw new ItemNotAvailableException(itemId);
        }
        if (patron.hasUnpaidFine()) {
            throw new OutstandingFineException(patronId, patron.getUnpaidFine());
        }
        int activeLoanCount = loans.findActiveLoansOf(patronId).size();
        if (patron.hasReachedBorrowingLimit(activeLoanCount)) {
            throw new BorrowLimitExceededException(patronId, patron.getBorrowingLimit());
        }

        Loan loan = new Loan(nextLoanId(), item, patron, borrowDate);
        loans.save(loan);
        return loan;
    }

    /**
     * Takes an item back and closes its loan.
     *
     * @return the closed loan
     */
    public Loan returnItem(String loanId, LocalDate returnDate) {
        Loan loan = loans.findById(loanId);

        loan.markReturned(returnDate);
        loans.save(loan);
        return loan;
    }

    /** Loan ids are L-1, L-2, L-3 ... so tests always know the next id. */
    private String nextLoanId() {
        String id = "L-" + nextLoanNumber;
        nextLoanNumber++;
        return id;
    }
}
