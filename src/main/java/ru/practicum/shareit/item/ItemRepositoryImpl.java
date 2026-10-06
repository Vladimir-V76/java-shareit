package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private static final Map<Long, Item> itemById = new HashMap<>();
    private static final Map<Long, Item> itemByOwner = new HashMap<>();

    @Override
    public Item create(Item item) {
        long id = 1L;
        if (!itemById.isEmpty()) {
            id = itemById.keySet().stream().max(Comparator.naturalOrder()).orElse(0L);
            id = id + 1;
        }
        item.setId(id);
        itemByOwner.put(item.getOwnerId(), item);
        itemById.put(id, item);

        return item;
    }

    @Override
    public Item findById(Long id) {
        return null;
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return List.of();
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
