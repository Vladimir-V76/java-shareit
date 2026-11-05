package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

@Service
public class BookingMapper {

    public static Booking mapToBooking(NewBookingRequest newBooking, Item item, Long bookerId) {
        if (newBooking.getEnd().isBefore(newBooking.getStart())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }
        Booking booking = new Booking();
        booking.setStart(newBooking.getStart());
        booking.setEnd(newBooking.getEnd());
        booking.setItem(item);
        booking.setBookerId(bookerId);
        booking.setStatus(newBooking.getStatus());

        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBookerId(),
                booking.getStatus().toString());
    }
}
