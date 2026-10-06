package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class ItemDto {
    @NotBlank(message = "Имя должно быть указано")
    String name;

    String description;

    @NotBlank(message = "Статус занятости должен быть указан")
    String available;
}
