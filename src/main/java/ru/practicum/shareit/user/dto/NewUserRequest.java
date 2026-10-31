package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректный формат email")
    String email;

    String name;
}
