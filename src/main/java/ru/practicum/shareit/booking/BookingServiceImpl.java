package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenItemUpdateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private static final String[] STATES = {"all", "current", "past", "future", "waiting", "rejected"};

    @Override
    @Transactional
    public BookingDto create(NewBookingRequest newBooking, Long userId) {
        Item item = findItemById(newBooking.getItemId());
        findUserById(userId);
        return BookingMapper.mapToBookingDto(bookingRepository.save(BookingMapper.mapToBooking(newBooking, item, userId)));
    }

    @Override
    @Transactional
    public BookingDto approvedBooking(Long bookingId, Long userId, String approved) {
        Booking booking = findBookingById(bookingId);
        findUserById(userId);
        if (!Objects.equals(userId, booking.getItem().getOwnerId())) {
            throw new ForbiddenItemUpdateException("Одобрять или отказывать в аренде вещей может только владелец");
        }
        switch (approved.toLowerCase()) {
            case "true":
                booking.setStatus(BookingStatus.APPROVED);
                break;
            case "false":
                booking.setStatus(BookingStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Значение approved может быть только true или false");
        }
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        Booking booking = findBookingById(bookingId);
        if (!Objects.equals(userId, booking.getBookerId()) && !Objects.equals(userId, booking.getItem().getOwnerId())) {
            String message = "Данные о бронировании могут получить владелец вещи или лицо осуществившее бронирование";
            throw new ForbiddenItemUpdateException(message);
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> findByUserIdAndStatus(Long userId, String state) {
        if (!Arrays.asList(STATES).contains(state.toLowerCase())) {
            throw new ValidationException("Значение state может быть: ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED");
        }
        findUserById(userId);

        return List.of();
    }


    private Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id: " + bookingId + " не найдена"));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена"));
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
    }
}
