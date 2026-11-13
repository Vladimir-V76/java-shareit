package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(NewItemRequest newItem, Long ownerId);

    ItemDto findById(Long itemId, Long userId);

    List<ItemBookingDatesDto> findAllByOwnerId(Long ownerId);

    ItemDto update(UpdateItemRequest updateItem, Long ownerId, Long itemId);

    void delete(Long itemId, Long ownerId);

    List<ItemWithoutCommentDto> searchItems(String searchText);

    CommentDto addCommentByItemId(Long itemId, Long userId, NewCommentRequest newComment);
}
