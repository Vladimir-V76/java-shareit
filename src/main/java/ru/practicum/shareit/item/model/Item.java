package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Item {
    Long id;
    Long ownerId;
    String name;
    String description;
    Boolean available;
}
