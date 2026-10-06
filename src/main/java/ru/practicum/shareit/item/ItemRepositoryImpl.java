package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private static final Map<Long, Item> itemById = new HashMap<>();
    private static final Map<Long, List<Item>> itemByOwner = new HashMap<>();

    @Override
    public Item create(Item item) {
        long id = 1L;
        if (!itemById.isEmpty()) {
            id = itemById.keySet().stream().max(Comparator.naturalOrder()).orElse(0L);
            id = id + 1;
        }
        item.setId(id);
        itemById.put(id, item);
        Long ownerId = item.getOwnerId();
        List<Item> items = new ArrayList<>();
        if (itemByOwner.containsKey(ownerId)) {
            items = itemByOwner.get(ownerId);
        }
        items.add(item);
        itemByOwner.put(ownerId, items);

        return item;
    }

    @Override
    public Item findById(Long itemId) {
        if (itemById.containsKey(itemId)) {
            return itemById.get(itemId);
        } else {
            throw new NotFoundException("Вещь с id: " + itemId + "не найдена");
        }
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        if (itemByOwner.containsKey(ownerId)) {
            return itemByOwner.get(ownerId);
        } else {
            throw new NotFoundException("У пользователя с id: " + ownerId + "нет вещей");
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
    public void delete(Item item) {
        itemById.remove(item.getId());
        List<Item> items = itemByOwner.get(item.getOwnerId());
        items.remove(item);
        itemByOwner.put(item.getOwnerId(), items);
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
