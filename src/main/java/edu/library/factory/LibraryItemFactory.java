package edu.library.factory;

import edu.library.model.item.Book;
import edu.library.model.item.Dvd;
import edu.library.model.item.ItemType;
import edu.library.model.item.LibraryItem;
import edu.library.model.item.Magazine;
import edu.library.validation.Check;

/**
 * Creates library items from a type.
 *
 * Factory pattern: the caller says WHAT it wants (ItemType.BOOK)
 * and does not need to know WHICH class to call "new" on.
 * Only this class knows the concrete classes Book, Magazine and Dvd.
 */
public class LibraryItemFactory {

    public LibraryItem create(ItemType type, String id, String title) {
        Check.notNull(type, "Item type");

        return switch (type) {
            case BOOK -> new Book(id, title);
            case MAGAZINE -> new Magazine(id, title);
            case DVD -> new Dvd(id, title);
        };
    }
}
