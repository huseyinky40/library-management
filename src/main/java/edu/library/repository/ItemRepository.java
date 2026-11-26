package edu.library.repository;

import java.util.List;

import edu.library.model.item.LibraryItem;

/**
 * Stores the items of the library.
 *
 * Services only know this interface, not how the items are stored.
 * Today the items live in memory (InMemoryItemRepository); a database
 * version could be added later without changing any service.
 */
public interface ItemRepository {

    /** Adds a new item. Throws DuplicateItemException if the id is already used. */
    void save(LibraryItem item);

    /** Finds an item by id. Throws ItemNotFoundException if there is none. */
    LibraryItem findById(String itemId);

    boolean existsById(String itemId);

    /** All items, in the order they were added. */
    List<LibraryItem> findAll();
}
