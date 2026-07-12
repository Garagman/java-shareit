package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemShortDtoTest {

    @Test
    void constructor_shouldSetAllFields() {
        ItemShortDto dto = new ItemShortDto();
        dto.setId(1L);
        dto.setName("Test Item");
        dto.setOwnerId(2L);

        assertEquals(1L, dto.getId());
        assertEquals("Test Item", dto.getName());
        assertEquals(2L, dto.getOwnerId());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        ItemShortDto dto = new ItemShortDto(1L, "Test Item", 2L);

        assertEquals(1L, dto.getId());
        assertEquals("Test Item", dto.getName());
        assertEquals(2L, dto.getOwnerId());
    }

    @Test
    void equals_shouldWorkCorrectly() {
        ItemShortDto dto1 = new ItemShortDto(1L, "Test", 2L);
        ItemShortDto dto2 = new ItemShortDto(1L, "Test", 2L);

        assertEquals(dto1, dto2);
    }

    @Test
    void hashCode_shouldWorkCorrectly() {
        ItemShortDto dto1 = new ItemShortDto(1L, "Test", 2L);
        ItemShortDto dto2 = new ItemShortDto(1L, "Test", 2L);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        ItemShortDto dto = new ItemShortDto(1L, "Test", 2L);
        String str = dto.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("Test"));
        assertTrue(str.contains("2"));
    }
}