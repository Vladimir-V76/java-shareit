package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

@Service
public class ItemMapper {

    public static Item mapToItem(NewItemRequest newItem, Long ownerId) {
        Item item = new Item();
        item.setOwnerId(ownerId);
        item.setName(newItem.getName());
        item.setDescription(newItem.getDescription());
        item.setAvailable(newItem.getAvailable());

        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public static void updateItemFields(Item item, UpdateItemRequest updateItem) {
        if (updateItem.hasName()) {
            item.setName(updateItem.getName());
        }
        if (updateItem.hasDescription()) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.hasAvailable()) {
            item.setAvailable(updateItem.getAvailable());
        }
    }
}
