package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    @Test
    void defaultConstructor_shouldCreateEmptyItemRequest() {
        ItemRequest request = new ItemRequest();

        assertNotNull(request);
        assertNull(request.getId());
        assertNull(request.getDescription());
        assertNull(request.getCreated());
        assertNull(request.getRequester());
    }

    @Test
    void setters_shouldUpdateFields() {
        ItemRequest request = new ItemRequest();
        User requester = new User(1L, "Requester", "requester@test.com");
        LocalDateTime created = LocalDateTime.now();

        request.setId(1L);
        request.setDescription("Need a drill");
        request.setCreated(created);
        request.setRequester(requester);

        assertEquals(1L, request.getId());
        assertEquals("Need a drill", request.getDescription());
        assertEquals(created, request.getCreated());
        assertEquals(requester, request.getRequester());
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);

        assertEquals(request, request);
    }

    @Test
    void equals_shouldReturnTrueForEqualObjects() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setDescription("Test");

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);
        request2.setDescription("Test");

        assertEquals(request1, request2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentObjects() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(2L);

        assertNotEquals(request1, request2);
    }

    @Test
    void hashCode_shouldReturnSameValueForEqualObjects() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need a drill");

        String str = request.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("Need a drill"));
    }
}