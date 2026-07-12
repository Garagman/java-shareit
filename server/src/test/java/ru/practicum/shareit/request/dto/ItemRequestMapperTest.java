package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void toItemRequestDto_shouldMapAllFields() {
        User requester = new User();
        requester.setId(1L);

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need a drill");
        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);

        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Need a drill", dto.getDescription());
        assertNotNull(dto.getCreated());
        assertEquals(1L, dto.getRequesterId());
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }

    @Test
    void toItemRequest_shouldMapAllFields() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a drill");

        User requester = new User();
        requester.setId(1L);

        ItemRequest request = ItemRequestMapper.toItemRequest(dto, requester);

        assertNotNull(request);
        assertEquals("Need a drill", request.getDescription());
        assertEquals(requester, request.getRequester());
        assertNotNull(request.getCreated());
    }

    @Test
    void toItemRequestDto_withNull_shouldReturnNull() {
        assertNull(ItemRequestMapper.toItemRequestDto(null, Collections.emptyList()));
    }

    @Test
    void toItemRequest_withNull_shouldReturnNull() {
        assertNull(ItemRequestMapper.toItemRequest(null, new User()));
    }
}