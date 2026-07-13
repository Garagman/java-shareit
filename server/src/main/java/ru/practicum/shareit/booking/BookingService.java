package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto create(BookingDto bookingDto, Long bookerId);

    BookingDto updateStatus(Long bookingId, Long ownerId, Boolean approved);

    BookingDto findById(Long bookingId, Long userId);

    List<BookingDto> findAllByBooker(Long bookerId, State state, int from, int size);

    List<BookingDto> findAllByOwner(Long ownerId, State state, int from, int size);
}