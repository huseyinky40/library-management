package edu.library.factory;

import edu.library.model.patron.Patron;
import edu.library.model.patron.PatronType;
import edu.library.policy.FacultyPolicy;
import edu.library.policy.PatronPolicy;
import edu.library.policy.StudentPolicy;
import edu.library.validation.Check;

/**
 * Creates patrons with the right policy for their type.
 *
 * Factory pattern: the caller only says PatronType.STUDENT,
 * and this class picks the matching PatronPolicy.
 */
public class PatronFactory {

    public Patron create(PatronType type, String id, String name) {
        Check.notNull(type, "Patron type");

        return new Patron(id, name, policyFor(type));
    }

    private PatronPolicy policyFor(PatronType type) {
        return switch (type) {
            case STUDENT -> new StudentPolicy();
            case FACULTY -> new FacultyPolicy();
        };
    }
}
