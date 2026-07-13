package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemBookingInfoTest {

    @Test
    void constructor_shouldSetAllFields() {
        ItemBookingInfo info = new ItemBookingInfo(1L, 2L);

        assertNotNull(info);
        assertEquals(1L, info.getId());
        assertEquals(2L, info.getBookerId());
    }

    @Test
    void getters_shouldReturnCorrectValues() {
        ItemBookingInfo info = new ItemBookingInfo();
        info.setId(1L);
        info.setBookerId(2L);

        assertEquals(1L, info.getId());
        assertEquals(2L, info.getBookerId());
    }

    @Test
    void equals_shouldWorkCorrectly() {
        ItemBookingInfo info1 = new ItemBookingInfo(1L, 2L);
        ItemBookingInfo info2 = new ItemBookingInfo(1L, 2L);

        assertEquals(info1, info2);
    }

    @Test
    void hashCode_shouldWorkCorrectly() {
        ItemBookingInfo info1 = new ItemBookingInfo(1L, 2L);
        ItemBookingInfo info2 = new ItemBookingInfo(1L, 2L);

        assertEquals(info1.hashCode(), info2.hashCode());
    }
}