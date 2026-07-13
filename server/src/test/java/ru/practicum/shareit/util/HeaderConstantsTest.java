package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeaderConstantsTest {

    @Test
    void userIdHeader_shouldHaveCorrectValue() {
        assertEquals("X-Sharer-User-Id", HeaderConstants.USER_ID_HEADER);
    }

    @Test
    void userIdHeader_shouldNotBeNull() {
        assertNotNull(HeaderConstants.USER_ID_HEADER);
    }
}