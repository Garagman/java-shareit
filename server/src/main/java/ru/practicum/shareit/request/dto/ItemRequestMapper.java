package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request, List<Item> items) {
        if (request == null) {
            return null;
        }
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setRequesterId(request.getRequester().getId());
        dto.setItems(items.stream()
                .map(ItemMapper::toItemShortDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto dto, User requester) {
        if (dto == null) {
            return null;
        }
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);
        return request;
    }
}