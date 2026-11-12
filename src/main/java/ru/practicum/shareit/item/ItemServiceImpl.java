package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenItemUpdateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;
    public final UserRepository userRepository;
    public final BookingRepository bookingRepository;
    public final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto create(NewItemRequest newItem, Long ownerId) {
        User user = findUserById(ownerId);
        return ItemMapper.mapToItemDto(
                itemRepository.save(ItemMapper.mapToItem(newItem, user)), List.of(), new ItemBookingDatesDto());
    }

    @Override
    public ItemDto findById(Long itemId, Long userId) {
        Item item = findItemById(itemId);
        List<CommentDto> comments = ItemMapper.mapToListCommentDto(commentRepository.findByItemId(itemId));
        return ItemMapper.mapToItemDto(item, comments, getLastBookingByItemId(itemId, userId));
    }

    @Override
    public List<ItemBookingDatesDto> findAllByOwnerId(Long ownerId) {
        findUserById(ownerId);
        List<Long> itemIds = itemRepository.findAllByOwnerId(ownerId).stream().map(Item::getId).toList();
        if (itemIds.isEmpty()) {
            return List.of();
        }
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findLastAndNextEnds(itemIds, now);
    }

    @Override
    @Transactional
    public ItemDto update(UpdateItemRequest updateItem, Long ownerId, Long itemId) {
        findUserById(ownerId);
        Item item = findItemById(itemId);
        if (!Objects.equals(ownerId, item.getOwner().getId())) {
            throw new ForbiddenItemUpdateException("Обновлять данные о вещах может только владелец");
        }
        ItemMapper.updateItemFields(item, updateItem);
        List<CommentDto> comments = ItemMapper.mapToListCommentDto(commentRepository.findByItemId(itemId));
        return ItemMapper.mapToItemDto(itemRepository.save(item), comments, getLastBookingByItemId(itemId, ownerId));
    }

    @Override
    @Transactional
    public void delete(Long itemId, Long ownerId) {
        findUserById(ownerId);
        Item item = findItemById(itemId);
        if (!Objects.equals(ownerId, item.getOwner().getId())) {
            throw new ForbiddenItemUpdateException("Удалять данные о вещах может только владелец");
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemWithoutCommentDto> searchItems(String searchText) {
        if (searchText.isBlank()) {
            return List.of();
        }
        return itemRepository.searchItems(searchText.toLowerCase()).stream()
                .map(ItemMapper::mapToItemWithoutCommentDto).toList();
    }

    @Override
    @Transactional
    public CommentDto addCommentByItemId(Long itemId, Long userId, NewCommentRequest newComment) {
        Item item = findItemById(itemId);
        User user = findUserById(userId);
        bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBeforeOrderByEndDesc(
                itemId,
                userId,
                BookingStatus.APPROVED,
                LocalDateTime.now()
        ).orElseThrow(() -> {
            String message = "Оставлять комментарии о вещах может только тот, кто их брал в аренду";
            return new ValidationException(message);
        });
        Comment comment = ItemMapper.mapToComment(item, user, newComment);
        return ItemMapper.mapToCommentDto(commentRepository.save(comment));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена"));
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
    }

    private ItemBookingDatesDto getLastBookingByItemId(Long itemId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        ItemBookingDatesDto item = bookingRepository.findLastAndNextEndsByItemId(itemId, now)
                .orElse(new ItemBookingDatesDto());
        if (!Objects.equals(item.getOwnerId(), userId)) {
            return new ItemBookingDatesDto();
        }
        return item;
    }
}