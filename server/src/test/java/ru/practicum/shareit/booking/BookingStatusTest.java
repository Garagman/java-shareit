package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingStatusTest {

    @Test
    void valueOf_withValidStatus_shouldReturnStatus() {
        assertEquals(BookingStatus.WAITING, BookingStatus.valueOf("WAITING"));
        assertEquals(BookingStatus.APPROVED, BookingStatus.valueOf("APPROVED"));
        assertEquals(BookingStatus.REJECTED, BookingStatus.valueOf("REJECTED"));
    }

    @Test
    void valueOf_withInvalidStatus_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> BookingStatus.valueOf("INVALID"));
    }

    @Test
    void values_shouldReturnAllStatuses() {
        BookingStatus[] values = BookingStatus.values();
        assertEquals(3, values.length);
    }
}