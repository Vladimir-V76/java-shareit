package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

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
        return null;
    }

    @Override
    public List<ItemDto> findAllByOwnerId(Long ownerId) {
        return List.of();
    }

    @Override
    public ItemDto update(UpdateItemRequest updateItem, Long ownerId, Long itemId) {
        return null;
    }

    @Override
    public void delete(Long itemId, Long ownerId) {

    }
}
