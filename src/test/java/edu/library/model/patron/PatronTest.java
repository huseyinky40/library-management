package edu.library.model.patron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import edu.library.exception.InvalidInputException;
import edu.library.exception.InvalidPaymentException;
import edu.library.policy.FacultyPolicy;
import edu.library.policy.StudentPolicy;

class PatronTest {

    private Patron newStudent() {
        return new Patron("P-1", "Ayşe", new StudentPolicy());
    }

    private Patron newFaculty() {
        return new Patron("P-2", "Mehmet", new FacultyPolicy());
    }

    // ---------- creating a patron ----------

    @Test
    void newPatronKeepsIdAndName() {
        Patron patron = newStudent();

        assertEquals("P-1", patron.getId());
        assertEquals("Ayşe", patron.getName());
    }

    @Test
    void newPatronHasNoUnpaidFine() {
        Patron patron = newStudent();

        assertEquals(new BigDecimal("0.00"), patron.getUnpaidFine());
        assertFalse(patron.hasUnpaidFine());
    }

    @Test
    void patronWithEmptyNameCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new Patron("P-1", "  ", new StudentPolicy())
        );

        assertEquals("Patron name must not be empty", exception.getMessage());
    }

    @Test
    void patronWithoutIdCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new Patron(null, "Ayşe", new StudentPolicy())
        );

        assertEquals("Patron id must not be empty", exception.getMessage());
    }

    @Test
    void patronWithoutPolicyCannotBeCreated() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> new Patron("P-1", "Ayşe", null)
        );

        assertEquals("Patron policy must not be null", exception.getMessage());
    }

    // ---------- rules come from the policy ----------

    @Test
    void studentGetsStudentRules() {
        Patron student = newStudent();

        assertEquals(PatronType.STUDENT, student.getType());
        assertEquals(3, student.getBorrowingLimit());
        assertEquals(new BigDecimal("1.00"), student.getDailyFine());
    }

    @Test
    void facultyGetsFacultyRules() {
        Patron faculty = newFaculty();

        assertEquals(PatronType.FACULTY, faculty.getType());
        assertEquals(10, faculty.getBorrowingLimit());
        assertEquals(new BigDecimal("0.50"), faculty.getDailyFine());
    }

    // ---------- hasReachedBorrowingLimit ----------

    @Test
    void studentBelowLimitCanBorrowMore() {
        Patron student = newStudent();

        assertFalse(student.hasReachedBorrowingLimit(2));
    }

    @Test
    void studentWithThreeLoansHasReachedLimit() {
        Patron student = newStudent();

        assertTrue(student.hasReachedBorrowingLimit(3));
    }

    @Test
    void facultyWithThreeLoansCanBorrowMore() {
        Patron faculty = newFaculty();

        assertFalse(faculty.hasReachedBorrowingLimit(3));
    }

    @Test
    void negativeLoanCountThrows() {
        Patron student = newStudent();

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> student.hasReachedBorrowingLimit(-1)
        );

        assertEquals("Active loan count must not be negative", exception.getMessage());
    }

    // ---------- addFine ----------

    @Test
    void addFineIncreasesUnpaidFine() {
        Patron patron = newStudent();

        patron.addFine(new BigDecimal("3"));

        assertEquals(new BigDecimal("3.00"), patron.getUnpaidFine());
        assertTrue(patron.hasUnpaidFine());
    }

    @Test
    void finesAddUp() {
        Patron patron = newStudent();

        patron.addFine(new BigDecimal("2.50"));
        patron.addFine(new BigDecimal("1.00"));

        assertEquals(new BigDecimal("3.50"), patron.getUnpaidFine());
    }

    @Test
    void addingZeroFineThrows() {
        Patron patron = newStudent();

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> patron.addFine(BigDecimal.ZERO)
        );

        assertEquals("Fine amount must be positive", exception.getMessage());
    }

    @Test
    void addingNullFineThrows() {
        Patron patron = newStudent();

        assertThrows(InvalidInputException.class, () -> patron.addFine(null));
    }

    // ---------- payFine ----------

    @Test
    void payingPartOfTheFineLeavesTheRest() {
        Patron patron = newStudent();
        patron.addFine(new BigDecimal("5.00"));

        patron.payFine(new BigDecimal("2.00"));

        assertEquals(new BigDecimal("3.00"), patron.getUnpaidFine());
    }

    @Test
    void payingTheWholeFineClearsIt() {
        Patron patron = newStudent();
        patron.addFine(new BigDecimal("5.00"));

        patron.payFine(new BigDecimal("5.00"));

        assertEquals(new BigDecimal("0.00"), patron.getUnpaidFine());
        assertFalse(patron.hasUnpaidFine());
    }

    @Test
    void payingMoreThanTheFineThrows() {
        Patron patron = newStudent();
        patron.addFine(new BigDecimal("5.00"));

        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> patron.payFine(new BigDecimal("8"))
        );

        assertEquals("Payment of 8.00 is more than the unpaid fine of 5.00", exception.getMessage());
    }

    @Test
    void failedPaymentDoesNotChangeTheFine() {
        Patron patron = newStudent();
        patron.addFine(new BigDecimal("5.00"));

        assertThrows(InvalidPaymentException.class, () -> patron.payFine(new BigDecimal("8")));

        assertEquals(new BigDecimal("5.00"), patron.getUnpaidFine());
    }

    @Test
    void payingWhenNothingIsOwedThrows() {
        Patron patron = newStudent();

        assertThrows(InvalidPaymentException.class, () -> patron.payFine(new BigDecimal("1")));
    }

    @Test
    void payingNegativeAmountThrows() {
        Patron patron = newStudent();
        patron.addFine(new BigDecimal("5.00"));

        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> patron.payFine(new BigDecimal("-1"))
        );

        assertEquals("Payment amount must be positive", exception.getMessage());
    }

    @Test
    void payingNullAmountThrows() {
        Patron patron = newStudent();

        assertThrows(InvalidPaymentException.class, () -> patron.payFine(null));
    }

    // ---------- equals and toString ----------

    @Test
    void patronsWithSameIdAreEqual() {
        Patron first = new Patron("P-1", "Ayşe", new StudentPolicy());
        Patron second = new Patron("P-1", "Ayşe Yılmaz", new StudentPolicy());

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void patronsWithDifferentIdsAreNotEqual() {
        assertNotEquals(newStudent(), newFaculty());
    }

    @Test
    void toStringShowsTypeIdAndName() {
        assertEquals("STUDENT(P-1, Ayşe)", newStudent().toString());
    }
}
