package edu.library.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.library.exception.DuplicateItemException;
import edu.library.exception.ItemNotFoundException;
import edu.library.model.item.LibraryItem;
import edu.library.validation.Check;

/**
 * Keeps the items in a Map in memory: id -> item.
 * LinkedHashMap remembers the order the items were added.
 */
public class InMemoryItemRepository implements ItemRepository {

    private final Map<String, LibraryItem> itemsById = new LinkedHashMap<>();

    @Override
    public void save(LibraryItem item) {
        Check.notNull(item, "Item");

        if (existsById(item.getId())) {
            throw new DuplicateItemException(item.getId());
        }
        itemsById.put(item.getId(), item);
    }

    @Override
    public LibraryItem findById(String itemId) {
        LibraryItem item = itemsById.get(itemId);

        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }
        return item;
    }

    @Override
    public boolean existsById(String itemId) {
        return itemsById.containsKey(itemId);
    }

    @Override
    public List<LibraryItem> findAll() {
        return List.copyOf(itemsById.values());   // a copy, so callers cannot change our map
    }
}
