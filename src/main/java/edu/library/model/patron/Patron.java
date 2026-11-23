package edu.library.model.patron;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import edu.library.exception.InvalidInputException;
import edu.library.exception.InvalidPaymentException;
import edu.library.policy.PatronPolicy;
import edu.library.validation.Check;

/**
 * A library member who can borrow items.
 *
 * The borrowing limit and the daily fine come from the PatronPolicy
 * (Strategy pattern), so this class works the same for students and faculty.
 *
 * The patron also keeps the total of their unpaid fines.
 * Money is stored as BigDecimal with 2 decimals, because double
 * cannot store values like 0.10 exactly.
 */
public class Patron {

    private static final int MONEY_SCALE = 2;
    private static final BigDecimal NO_FINE = money(BigDecimal.ZERO);

    private final String id;
    private final String name;
    private final PatronPolicy policy;
    private BigDecimal unpaidFine = NO_FINE;

    public Patron(String id, String name, PatronPolicy policy) {
        this.id = Check.notBlank(id, "Patron id");
        this.name = Check.notBlank(name, "Patron name");
        this.policy = Check.notNull(policy, "Patron policy");
    }

    // ---------- rules from the policy ----------

    public int getBorrowingLimit() {
        return policy.getBorrowingLimit();
    }

    public BigDecimal getDailyFine() {
        return policy.getDailyFine();
    }

    public PatronType getType() {
        return policy.getType();
    }

    /**
     * Checks if the patron already has as many items as they are allowed.
     *
     * @param activeLoanCount how many items the patron has on loan right now
     */
    public boolean hasReachedBorrowingLimit(int activeLoanCount) {
        if (activeLoanCount < 0) {
            throw new InvalidInputException("Active loan count must not be negative");
        }
        return activeLoanCount >= getBorrowingLimit();
    }

    // ---------- fines ----------

    public boolean hasUnpaidFine() {
        return unpaidFine.signum() > 0;
    }

    /** Adds a late fine to the patron's unpaid total. */
    public void addFine(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidInputException("Fine amount must be positive");
        }
        unpaidFine = money(unpaidFine.add(amount));
    }

    /**
     * Pays some or all of the unpaid fine.
     * The patron cannot pay zero, a negative amount, or more than they owe.
     */
    public void payFine(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidPaymentException("Payment amount must be positive");
        }
        if (amount.compareTo(unpaidFine) > 0) {
            throw new InvalidPaymentException(
                    "Payment of " + money(amount).toPlainString()
                            + " is more than the unpaid fine of " + unpaidFine.toPlainString());
        }
        unpaidFine = money(unpaidFine.subtract(amount));
    }

    // ---------- getters ----------

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnpaidFine() {
        return unpaidFine;
    }

    // ---------- equals, hashCode, toString ----------

    /** Two patrons are the same patron when they have the same id. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Patron patron = (Patron) other;
        return id.equals(patron.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getType() + "(" + id + ", " + name + ")";
    }

    /** Rounds a money value to 2 decimals, for example 3 becomes 3.00. */
    private static BigDecimal money(BigDecimal value) {
        return value.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
