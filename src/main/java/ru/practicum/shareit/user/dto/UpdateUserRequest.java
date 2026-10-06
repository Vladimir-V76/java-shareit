package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String email;
    String name;

    public boolean hasEmail() {
        return (email != null && !email.isBlank());
    }

    public boolean hasName() {
        return (name != null && !name.isBlank());
    }
}

