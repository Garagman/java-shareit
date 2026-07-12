package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    @Test
    void toBookingDto_shouldMapAllFields() {
        User booker = new User();
        booker.setId(1L);

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto dto = BookingMapper.toBookingDto(booking);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNotNull(dto.getStart());
        assertNotNull(dto.getEnd());
        assertEquals(1L, dto.getItemId());
        assertEquals(1L, dto.getBookerId());
        assertEquals("WAITING", dto.getStatus());
    }

    @Test
    void toBooking_shouldMapAllFields() {
        User booker = new User();
        booker.setId(1L);

        Item item = new Item();
        item.setId(1L);

        BookingDto dto = new BookingDto();
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = BookingMapper.toBooking(dto, item, booker);

        assertNotNull(booking);
        assertNotNull(booking.getStart());
        assertNotNull(booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void toBookingDto_withNull_shouldReturnNull() {
        assertNull(BookingMapper.toBookingDto(null));
    }

    @Test
    void toBooking_withNull_shouldReturnNull() {
        assertNull(BookingMapper.toBooking(null, new Item(), new User()));
    }
}