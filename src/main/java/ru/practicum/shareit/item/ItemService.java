package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ItemService {
    ItemDto create(NewItemRequest newItem, Long ownerId);

    ItemDto findById(Long itemId);

    List<ItemDto> findAllByOwnerId(Long ownerId);

    ItemDto update(UpdateItemRequest updateItem, Long ownerId, Long itemId) throws AccessDeniedException;

    void delete(Long itemId, Long ownerId) throws AccessDeniedException;

    List<ItemDto> searchItems(String searchText);
}
