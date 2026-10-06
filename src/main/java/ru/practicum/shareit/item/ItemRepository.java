package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemRepository {
    Item create(Item item);
    Item findById(Long itemId);
    List<Item> findAllByOwnerId(Long ownerId);
    Item update(Item item);
    void delete(Long id);
}
