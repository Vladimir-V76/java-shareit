package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookingDatesDto {
    Long itemId;
    Long ownerId;
    String name;
    String description;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
}
