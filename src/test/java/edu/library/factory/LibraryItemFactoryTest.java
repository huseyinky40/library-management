package edu.library.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.library.exception.InvalidInputException;
import edu.library.model.item.Book;
import edu.library.model.item.Dvd;
import edu.library.model.item.ItemType;
import edu.library.model.item.LibraryItem;
import edu.library.model.item.Magazine;

class LibraryItemFactoryTest {

    private final LibraryItemFactory factory = new LibraryItemFactory();

    @Test
    void createsBookForBookType() {
        LibraryItem item = factory.create(ItemType.BOOK, "B-1", "Dune");

        assertInstanceOf(Book.class, item);
    }

    @Test
    void createsMagazineForMagazineType() {
        LibraryItem item = factory.create(ItemType.MAGAZINE, "M-1", "Time");

        assertInstanceOf(Magazine.class, item);
    }

    @Test
    void createsDvdForDvdType() {
        LibraryItem item = factory.create(ItemType.DVD, "D-1", "Inception");

        assertInstanceOf(Dvd.class, item);
    }

    @Test
    void createdItemKeepsIdAndTitle() {
        LibraryItem item = factory.create(ItemType.BOOK, "B-1", "Dune");

        assertEquals("B-1", item.getId());
        assertEquals("Dune", item.getTitle());
    }

    @Test
    void creatingWithoutTypeThrows() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> factory.create(null, "B-1", "Dune")
        );

        assertEquals("Item type must not be null", exception.getMessage());
    }

    @Test
    void creatingWithEmptyTitleThrows() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> factory.create(ItemType.DVD, "D-1", " ")
        );

        assertEquals("Title must not be empty", exception.getMessage());
    }
}
