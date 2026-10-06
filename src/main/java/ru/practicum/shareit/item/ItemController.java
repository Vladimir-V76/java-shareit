package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;

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
            @Positive(message = "Id должно быть числом положительным")
            @RequestHeader("X-Later-User-Id")
            Long userId,
            @Valid @RequestBody NewItemRequest item) {
        return itemService.create(item, userId);
    }

    @GetMapping("/{itemId}")
    @ResponseBody
    public ItemDto findById(
            @Positive(message = "Id должно быть числом положительным")
            @PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

}
