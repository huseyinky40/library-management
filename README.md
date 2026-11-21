# Library Management — Advanced OOP Final Project

A small library lending domain implemented as plain Java classes and JUnit 5 tests.
There is no `main` method and no user interface: the unit tests are the only way the code runs.

## Domain

- **Items:** books, DVDs, magazines — the loan period depends on the item type.
- **Patrons:** students and faculty — the borrowing limit and daily overdue fine depend on the patron type.
- **Operations:** add an item, register a patron, borrow, return, calculate an overdue fine, pay a fine.

## Requirements

- Java 25 (JDK)
- No Maven installation needed — the Maven Wrapper (`mvnw`) downloads it.

## Running the tests

```bash
./mvnw test
```

Coverage report (JaCoCo), written to `target/site/jacoco/index.html`:

```bash
./mvnw verify
```

## Project layout

```
src/main/java/edu/library   domain classes
src/test/java/edu/library   JUnit 5 tests
```
