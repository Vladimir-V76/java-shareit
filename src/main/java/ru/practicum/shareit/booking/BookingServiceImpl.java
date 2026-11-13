package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStateSearchQuery;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.exception.ForbiddenItemUpdateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto create(NewBookingRequest newBooking, Long userId) {
        Item item = findItemById(newBooking.getItemId());
        User user = findUserById(userId);
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с id=" + item.getId() + " не доступна для бронирования");
        }

        return BookingMapper.mapToBookingDto(bookingRepository.save(BookingMapper.mapToBooking(newBooking, item, user)));
    }

    @Override
    @Transactional
    public BookingDto approvedBooking(Long bookingId, Long userId, String approved) {
        Booking booking = findBookingById(bookingId);
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new ForbiddenItemUpdateException("Одобрять или отказывать в аренде вещей может только владелец");
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException(
                    "Дата окончания бронирования с id=" + bookingId + "уже прошла. Одобрение не актуально");
        }
        findUserById(userId);
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
        if (!Objects.equals(
                userId, booking.getBooker().getId()) && !Objects.equals(userId, booking.getItem().getOwner().getId())) {
            String message = "Данные о бронировании могут получить владелец вещи или лицо осуществившее бронирование";
            throw new ForbiddenItemUpdateException(message);
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> findBookingsByBookerIdAndStatus(Long userId, BookingStateSearchQuery state) {
        findUserById(userId);
        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(userId);
        BooleanExpression byState = choiceState(state);
        return Streamable.of(bookingRepository.findAll(byBookerId.and(byState), QBooking.booking.start.desc()))
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> findBookingsByOwnerIdAndStatus(Long userId, BookingStateSearchQuery state) {
        findUserById(userId);
        BooleanExpression byOwnerId = QBooking.booking.item.owner.id.eq(userId);
        BooleanExpression byState = choiceState(state);
        return Streamable.of(bookingRepository.findAll(byOwnerId.and(byState), QBooking.booking.start.desc()))
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
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

    private BooleanExpression choiceState(BookingStateSearchQuery state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (state) {
            case ALL -> QBooking.booking.status.in(BookingStatus.values());
            case CURRENT -> QBooking.booking.start.before(now)
                    .and(QBooking.booking.end.after(now));
            case PAST -> QBooking.booking.end.before(now);
            case FUTURE -> QBooking.booking.start.after(now);
            case WAITING -> QBooking.booking.status.eq(BookingStatus.WAITING);
            case REJECTED -> QBooking.booking.status.eq(BookingStatus.REJECTED);
        };
    }
}
