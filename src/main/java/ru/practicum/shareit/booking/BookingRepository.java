package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBookingDatesDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {
    @Query("""
            SELECT new ru.practicum.shareit.item.dto.ItemBookingDatesDto(
                   i.id,
                   i.owner.id,
                   i.name,
                   i.description,
                   MAX(CASE WHEN b.end <= ?2 THEN b.end END),
                   MIN(CASE WHEN b.start > ?2 THEN b.start END))
            FROM Item i
            LEFT JOIN Booking b ON b.item.id = i.id AND b.status = 'APPROVED'
            WHERE i.id IN ?1
            GROUP BY i.id, i.owner.id, i.name, i.description
            """)
    List<ItemBookingDatesDto> findLastAndNextEnds(List<Long> itemIds, LocalDateTime now);

    @Query("""
            SELECT new ru.practicum.shareit.item.dto.ItemBookingDatesDto(
                   i.id,
                   i.owner.id,
                   i.name,
                   i.description,
                   MAX(CASE WHEN b.end <= ?2 THEN b.end END),
                   MIN(CASE WHEN b.start > ?2 THEN b.start END))
            FROM Item i
            LEFT JOIN Booking b ON b.item.id = i.id AND b.status = 'APPROVED'
            WHERE i.id = ?1
            GROUP BY i.id, i.owner.id, i.name, i.description
            """)
    Optional<ItemBookingDatesDto> findLastAndNextEndsByItemId(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatusAndEndBeforeOrderByEndDesc(
            Long itemId,
            Long userId,
            BookingStatus status,
            LocalDateTime end
    );
}
