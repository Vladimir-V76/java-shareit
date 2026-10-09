package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

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
            @PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDto> findByOwnerId(
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

            @RequestBody UpdateItemRequest item) throws AccessDeniedException {
        return itemService.update(item, userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseBody
    public void delete(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,

            @Positive(message = "Id вещи должно быть числом положительным")
            @PathVariable Long itemId) throws AccessDeniedException {
        itemService.delete(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
