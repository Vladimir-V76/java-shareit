package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    LocalDate startBooking;
    LocalDate endBooking;
    Long itemId;
    boolean isConfirmed;
}
