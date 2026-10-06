package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Available;
import ru.practicum.shareit.item.model.Item;

@Service
public class ItemMapper {

    public static Item mapToItem(NewItemRequest newItem, Long ownerId) {
        Item item = new Item();
        item.setOwnerId(ownerId);
        item.setName(newItem.getName());
        item.setDescription(newItem.getDescription());
        Available available = Available.from(newItem.getAvailable());
        if (available == null) {
            String message = "Указан не верный тип занятости %s. Доступные значения: busy, free";
            throw new NotFoundException(String.format(message, newItem.getAvailable()));
        }
        item.setAvailable(available);

        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable().toString());

        return itemDto;
    }
}
