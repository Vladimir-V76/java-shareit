package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    List<CommentDto> comments = new ArrayList<>();

    public ItemBookingDatesDto(Long itemId, Long ownerId, String name, String description, LocalDateTime lastBooking,
                               LocalDateTime nextBooking) {
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = new ArrayList<>();
    }
}
