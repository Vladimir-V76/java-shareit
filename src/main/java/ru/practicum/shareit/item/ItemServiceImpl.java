package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenItemUpdateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;
    public final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto create(NewItemRequest newItem, Long ownerId) {
        userRepository.checkUserId(ownerId);
        return ItemMapper.mapToItemDto(itemRepository.create(ItemMapper.mapToItem(newItem, ownerId)));
    }

    @Override
    public ItemDto findById(Long itemId) {
        return ItemMapper.mapToItemDto(findItemById(itemId));
    }

    @Override
    public List<ItemDto> findAllByOwnerId(Long ownerId) {
        userRepository.checkUserId(ownerId);
        return itemRepository.findAllByOwnerId(ownerId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    public ItemDto update(UpdateItemRequest updateItem, Long ownerId, Long itemId) {
        userRepository.checkUserId(ownerId);
        Item item = findItemById(itemId);
        if (!Objects.equals(ownerId, item.getOwnerId())) {
            throw new ForbiddenItemUpdateException("Обновлять данные о вещах может только владелец");
        }
        ItemMapper.updateItemFields(item, updateItem);
        return ItemMapper.mapToItemDto(itemRepository.update(item));
    }

    @Override
    public void delete(Long itemId, Long ownerId) {
        userRepository.checkUserId(ownerId);
        Item item = findItemById(itemId);
        if (!Objects.equals(ownerId, item.getOwnerId())) {
            throw new ForbiddenItemUpdateException("Удалять данные о вещах может только владелец");
        }
        itemRepository.deleteItemById(item);
    }

    @Override
    public List<ItemDto> searchItems(String searchText) {
        if (searchText.isBlank()) {
            return List.of();
        }
        return itemRepository.searchItems(searchText.toLowerCase()).stream().map(ItemMapper::mapToItemDto).toList();
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена"));
    }
}