package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Service
public interface ItemRepository {
    Item create(Item item);

    Optional<Item> findById(Long itemId);

    List<Item> findAllByOwnerId(Long ownerId);

    Item update(Item item);

    void deleteItemById(Item item);

    void deleteItemByOwnerId(Long ownerId);

    List<Item> searchItems(String searchText);
}
