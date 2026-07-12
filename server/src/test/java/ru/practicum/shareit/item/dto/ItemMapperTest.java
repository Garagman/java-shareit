package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void toItemDto_shouldMapAllFields() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);

        ItemDto dto = ItemMapper.toItemDto(item);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Item", dto.getName());
        assertEquals("Test Description", dto.getDescription());
        assertTrue(dto.getAvailable());
    }

    @Test
    void toItemDto_withBookingsAndComments_shouldMapAllFields() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);

        ItemBookingInfo lastBooking = new ItemBookingInfo(1L, 2L);
        ItemBookingInfo nextBooking = new ItemBookingInfo(2L, 3L);

        ItemDto dto = ItemMapper.toItemDto(item, lastBooking, nextBooking, Collections.emptyList());

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(lastBooking, dto.getLastBooking());
        assertEquals(nextBooking, dto.getNextBooking());
        assertNotNull(dto.getComments());
    }

    @Test
    void toItemShortDto_shouldMapAllFields() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setOwner(owner);

        ItemShortDto dto = ItemMapper.toItemShortDto(item);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Item", dto.getName());
        assertEquals(1L, dto.getOwnerId());
    }

    @Test
    void toItem_shouldMapAllFields() {
        User owner = new User();
        owner.setId(1L);

        ItemDto dto = new ItemDto();
        dto.setName("Test Item");
        dto.setDescription("Test Description");
        dto.setAvailable(true);

        Item item = ItemMapper.toItem(dto, owner);

        assertNotNull(item);
        assertEquals("Test Item", item.getName());
        assertEquals("Test Description", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
    }

    @Test
    void updateItemFields_shouldUpdateNonNullFields() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Old Name");
        item.setDescription("Old Description");
        item.setAvailable(false);
        item.setOwner(owner);

        ItemDto updateDto = new ItemDto();
        updateDto.setName("New Name");
        updateDto.setAvailable(true);

        ItemMapper.updateItemFields(item, updateDto);

        assertEquals("New Name", item.getName());
        assertEquals("Old Description", item.getDescription());
        assertTrue(item.getAvailable());
    }

    @Test
    void toItemDto_withNull_shouldReturnNull() {
        assertNull(ItemMapper.toItemDto(null));
    }

    @Test
    void toItemShortDto_withNull_shouldReturnNull() {
        assertNull(ItemMapper.toItemShortDto(null));
    }

    @Test
    void toItem_withNull_shouldReturnNull() {
        assertNull(ItemMapper.toItem(null, new User()));
    }
}