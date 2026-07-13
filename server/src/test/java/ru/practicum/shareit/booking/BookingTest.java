package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void defaultConstructor_shouldCreateEmptyBooking() {
        Booking booking = new Booking();

        assertNotNull(booking);
        assertNull(booking.getId());
        assertNull(booking.getStart());
        assertNull(booking.getEnd());
        assertNull(booking.getItem());
        assertNull(booking.getBooker());
        assertNull(booking.getStatus());
    }

    @Test
    void setters_shouldUpdateFields() {
        Booking booking = new Booking();
        User booker = new User(1L, "Booker", "booker@test.com");
        User owner = new User(2L, "Owner", "owner@test.com");
        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        assertEquals(1L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        Booking booking = new Booking();
        booking.setId(1L);

        assertEquals(booking, booking);
    }

    @Test
    void equals_shouldReturnTrueForEqualObjects() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        assertEquals(booking1, booking2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentObjects() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(2L);

        assertNotEquals(booking1, booking2);
    }

    @Test
    void hashCode_shouldReturnSameValueForEqualObjects() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        assertEquals(booking1.hashCode(), booking2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.WAITING);

        String str = booking.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("WAITING"));
    }
}