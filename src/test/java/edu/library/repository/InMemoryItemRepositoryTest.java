package edu.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.library.exception.DuplicateItemException;
import edu.library.exception.InvalidInputException;
import edu.library.exception.ItemNotFoundException;
import edu.library.model.item.Book;
import edu.library.model.item.Dvd;
import edu.library.model.item.LibraryItem;

class InMemoryItemRepositoryTest {

    private final ItemRepository repository = new InMemoryItemRepository();

    // ---------- save ----------

    @Test
    void savedItemCanBeFoundById() {
        LibraryItem book = new Book("B-1", "Dune");

        repository.save(book);

        assertSame(book, repository.findById("B-1"));
    }

    @Test
    void savingSameIdTwiceThrows() {
        repository.save(new Book("B-1", "Dune"));

        DuplicateItemException exception = assertThrows(
                DuplicateItemException.class,
                () -> repository.save(new Dvd("B-1", "Inception"))
        );

        assertEquals("Item already exists: B-1", exception.getMessage());
    }

    @Test
    void savingNullItemThrows() {
        assertThrows(InvalidInputException.class, () -> repository.save(null));
    }

    // ---------- findById ----------

    @Test
    void findingUnknownItemThrows() {
        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> repository.findById("X-9")
        );

        assertEquals("Item not found: X-9", exception.getMessage());
    }

    // ---------- existsById ----------

    @Test
    void existsByIdIsTrueAfterSaving() {
        repository.save(new Book("B-1", "Dune"));

        assertTrue(repository.existsById("B-1"));
    }

    @Test
    void existsByIdIsFalseForUnknownItem() {
        assertFalse(repository.existsById("X-9"));
    }

    // ---------- findAll ----------

    @Test
    void findAllReturnsItemsInTheOrderTheyWereAdded() {
        LibraryItem book = new Book("B-1", "Dune");
        LibraryItem dvd = new Dvd("D-1", "Inception");
        repository.save(book);
        repository.save(dvd);

        List<LibraryItem> items = repository.findAll();

        assertEquals(List.of(book, dvd), items);
    }

    @Test
    void findAllOfEmptyRepositoryIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void listFromFindAllCannotBeChanged() {
        repository.save(new Book("B-1", "Dune"));
        List<LibraryItem> items = repository.findAll();

        assertThrows(UnsupportedOperationException.class, () -> items.clear());
    }
}
