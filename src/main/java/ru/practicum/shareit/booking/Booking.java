package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;
    LocalDate startBooking;
    LocalDate endBooking;
    Long itemId;
    Long userId;
    boolean isConfirmed;
    Long reviewId;
}
