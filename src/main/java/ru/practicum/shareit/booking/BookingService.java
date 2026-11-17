package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.BookingStateSearchQuery;

import java.util.List;

public interface BookingService {
    BookingDto create(NewBookingRequest booking, Long userId);

    BookingDto approvedBooking(Long bookingId, Long userId, String approved);

    BookingDto findById(Long bookingId, Long userId);

    List<BookingDto> findBookingsByBookerIdAndStatus(Long userId, BookingStateSearchQuery state);

    List<BookingDto> findBookingsByOwnerIdAndStatus(Long userId, BookingStateSearchQuery state);
}
