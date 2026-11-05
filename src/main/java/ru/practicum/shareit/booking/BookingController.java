package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    public final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,

            @Valid @RequestBody NewBookingRequest booking) {
        return bookingService.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedBooking(
            @Positive(message = "Id бронирования должно быть числом положительным")
            @PathVariable Long bookingId,

            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,

            @NotBlank(message = "Статус бронирования должен быть указан")
            @RequestParam String approved) {
        return bookingService.approvedBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(
            @Positive(message = "Id бронирования должно быть числом положительным")
            @PathVariable Long bookingId,

            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findByUserIdAndStatus(
            @Positive(message = "Id пользователя должно быть числом положительным")
            @RequestHeader("X-Sharer-User-Id")
            Long userId,

            @RequestParam(defaultValue = "ALL")
            String state) {
        return bookingService.findByUserIdAndStatus(userId, state);
    }
}
