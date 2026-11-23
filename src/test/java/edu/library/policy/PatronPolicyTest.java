package edu.library.policy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import edu.library.model.patron.PatronType;

class PatronPolicyTest {

    // ---------- student ----------

    @Test
    void studentCanBorrowThreeItems() {
        PatronPolicy policy = new StudentPolicy();

        assertEquals(3, policy.getBorrowingLimit());
    }

    @Test
    void studentPaysOnePerLateDay() {
        PatronPolicy policy = new StudentPolicy();

        assertEquals(new BigDecimal("1.00"), policy.getDailyFine());
    }

    @Test
    void studentPolicyBelongsToStudents() {
        assertEquals(PatronType.STUDENT, new StudentPolicy().getType());
    }

    // ---------- faculty ----------

    @Test
    void facultyCanBorrowTenItems() {
        PatronPolicy policy = new FacultyPolicy();

        assertEquals(10, policy.getBorrowingLimit());
    }

    @Test
    void facultyPaysHalfPerLateDay() {
        PatronPolicy policy = new FacultyPolicy();

        assertEquals(new BigDecimal("0.50"), policy.getDailyFine());
    }

    @Test
    void facultyPolicyBelongsToFaculty() {
        assertEquals(PatronType.FACULTY, new FacultyPolicy().getType());
    }
}
