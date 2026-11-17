package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {
    public final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,
            @Valid @RequestBody NewItemRequest item) {
        return itemService.create(item, userId);
    }

    @GetMapping("/{itemId}")
    @ResponseBody
    public ItemDto findById(
            @Positive(message = "Id вещи должно быть числом положительным")
            @PathVariable Long itemId,

            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId) {
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemBookingDatesDto> findByOwnerId(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId) {

        return itemService.findAllByOwnerId(userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseBody
    public ItemDto update(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,

            @Positive(message = "Id вещи должно быть числом положительным")
            @PathVariable Long itemId,

            @RequestBody UpdateItemRequest item) {
        return itemService.update(item, userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseBody
    public void delete(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,

            @Positive(message = "Id вещи должно быть числом положительным")
            @PathVariable Long itemId) {
        itemService.delete(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemWithoutCommentDto> searchItem(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,

            @Positive(message = "Id вещи должно быть числом положительным")
            @PathVariable Long itemId,

            @Valid @RequestBody NewCommentRequest newComment) {
        return itemService.addCommentByItemId(itemId, userId, newComment);
    }


}
