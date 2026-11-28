package edu.library.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.library.exception.LoanNotFoundException;
import edu.library.model.loan.Loan;
import edu.library.validation.Check;

/**
 * Keeps the loans in a Map in memory: id -> loan.
 */
public class InMemoryLoanRepository implements LoanRepository {

    private final Map<String, Loan> loansById = new LinkedHashMap<>();

    @Override
    public void save(Loan loan) {
        Check.notNull(loan, "Loan");

        loansById.put(loan.getId(), loan);
    }

    @Override
    public Loan findById(String loanId) {
        Loan loan = loansById.get(loanId);

        if (loan == null) {
            throw new LoanNotFoundException(loanId);
        }
        return loan;
    }

    @Override
    public List<Loan> findActiveLoansOf(String patronId) {
        return loansById.values().stream()
                .filter(loan -> !loan.isReturned())
                .filter(loan -> loan.getPatron().getId().equals(patronId))
                .toList();
    }

    @Override
    public boolean isItemOnLoan(String itemId) {
        return loansById.values().stream()
                .filter(loan -> !loan.isReturned())
                .anyMatch(loan -> loan.getItem().getId().equals(itemId));
    }

    @Override
    public List<Loan> findAll() {
        return List.copyOf(loansById.values());
    }
}
