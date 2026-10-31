package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewItemRequest {

    @NotBlank(message = "Имя должно быть указано")
    String name;

    @NotBlank(message = "Описание не может быть пустым")
    String description;

    @NotNull(message = "Статус доступности должен быть указан")
    Boolean available;
}
