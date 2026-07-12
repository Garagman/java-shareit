package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void defaultConstructor_shouldCreateEmptyItem() {
        Item item = new Item();

        assertNotNull(item);
        assertNull(item.getId());
        assertNull(item.getName());
        assertNull(item.getDescription());
        assertNull(item.getAvailable());
        assertNull(item.getOwner());
        assertNull(item.getRequest());
    }

    @Test
    void setters_shouldUpdateFields() {
        Item item = new Item();
        User owner = new User(1L, "Owner", "owner@test.com");
        ItemRequest request = new ItemRequest();
        request.setId(1L);

        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);

        assertEquals(1L, item.getId());
        assertEquals("Test Item", item.getName());
        assertEquals("Test Description", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(request, item.getRequest());
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        Item item = new Item();
        item.setId(1L);

        assertEquals(item, item);
    }

    @Test
    void equals_shouldReturnTrueForEqualObjects() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Test");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Test");

        assertEquals(item1, item2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentObjects() {
        Item item1 = new Item();
        item1.setId(1L);

        Item item2 = new Item();
        item2.setId(2L);

        assertNotEquals(item1, item2);
    }

    @Test
    void hashCode_shouldReturnSameValueForEqualObjects() {
        Item item1 = new Item();
        item1.setId(1L);

        Item item2 = new Item();
        item2.setId(1L);

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        String str = item.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("Test Item"));
    }
}