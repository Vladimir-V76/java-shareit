package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;
    public final UserRepository userRepository;

    @Override
    @Transactional
    public ItemDto create(NewItemRequest newItem, Long ownerId) {
        findUserById(ownerId);
        return ItemMapper.mapToItemDto(itemRepository.save(ItemMapper.mapToItem(newItem, ownerId)));
    }

    @Override
    public ItemDto findById(Long itemId) {
        return ItemMapper.mapToItemDto(findItemById(itemId));
    }

    @Override
    public List<ItemDto> findAllByOwnerId(Long ownerId) {
        findUserById(ownerId);
        return itemRepository.findAllByOwnerId(ownerId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    @Transactional
    public ItemDto update(UpdateItemRequest updateItem, Long ownerId, Long itemId) {
        findUserById(ownerId);
        Item item = findItemById(itemId);
        if (!Objects.equals(ownerId, item.getOwnerId())) {
            throw new ForbiddenItemUpdateException("Обновлять данные о вещах может только владелец");
        }
        ItemMapper.updateItemFields(item, updateItem);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void delete(Long itemId, Long ownerId) {
        findUserById(ownerId);
        Item item = findItemById(itemId);
        if (!Objects.equals(ownerId, item.getOwnerId())) {
            throw new ForbiddenItemUpdateException("Удалять данные о вещах может только владелец");
        }
        itemRepository.deleteById(itemId);
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

    private void findUserById(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
    }
}