package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */

@Data
@EqualsAndHashCode(of = "email")
public class User {
    Long id;
    String email;
    String name;
}
