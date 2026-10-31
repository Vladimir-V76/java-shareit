package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private static final Map<Long, Item> itemById = new HashMap<>();
    private static final Map<Long, List<Item>> itemByOwner = new HashMap<>();
    private static Long id = 1L;

    @Override
    public Item create(Item item) {
        item.setId(id);
        itemById.put(id, item);
        Long ownerId = item.getOwnerId();
        List<Item> items = new ArrayList<>();
        if (itemByOwner.containsKey(ownerId)) {
            items = itemByOwner.get(ownerId);
        }
        items.add(item);
        itemByOwner.put(ownerId, items);
        id = id + 1;
        return item;
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(itemById.get(itemId));
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        if (itemByOwner.containsKey(ownerId)) {
            return itemByOwner.get(ownerId);
        } else {
            return List.of();
        }
    }

    @Override
    public Item update(Item item) {
        itemById.put(item.getId(), item);
        List<Item> items = itemByOwner.get(item.getOwnerId());
        items.remove(item);
        //if (items.contains(item)) { items.remove(item); }
        items.add(item);
        itemByOwner.put(item.getOwnerId(), items);
        return item;
    }

    @Override
    public void deleteItemById(Item item) {
        itemById.remove(item.getId());
        List<Item> items = itemByOwner.get(item.getOwnerId());
        items.remove(item);
        itemByOwner.put(item.getOwnerId(), items);
    }

    @Override
    public void deleteItemByOwnerId(Long ownerId) {
        itemByOwner.remove(ownerId);
        itemById.entrySet().removeIf(entry -> ownerId.equals(entry.getValue().getOwnerId()));
    }

    @Override
    public List<Item> searchItems(String searchText) {
        return itemById.values().stream()
                .filter(i -> i.getName().toLowerCase().contains(searchText) ||
                        i.getDescription().toLowerCase().contains(searchText))
                .filter(i -> i.getAvailable() == true)
                .toList();
    }
}