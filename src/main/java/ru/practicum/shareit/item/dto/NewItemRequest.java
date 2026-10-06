package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewItemRequest {

    @NotBlank(message = "Имя должно быть указано")
    String name;
    String description;

    @NotBlank(message = "Статус доступности должен быть указан")
    String available;
}
