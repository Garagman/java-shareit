package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void valueOf_withValidState_shouldReturnState() {
        assertEquals(State.ALL, State.valueOf("ALL"));
        assertEquals(State.CURRENT, State.valueOf("CURRENT"));
        assertEquals(State.PAST, State.valueOf("PAST"));
        assertEquals(State.FUTURE, State.valueOf("FUTURE"));
        assertEquals(State.WAITING, State.valueOf("WAITING"));
        assertEquals(State.REJECTED, State.valueOf("REJECTED"));
    }

    @Test
    void valueOf_withInvalidState_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> State.valueOf("INVALID"));
    }
}