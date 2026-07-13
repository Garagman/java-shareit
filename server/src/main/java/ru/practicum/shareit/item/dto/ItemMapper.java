package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static ItemDto toItemDto(Item item, ItemBookingInfo lastBooking, ItemBookingInfo nextBooking, List<CommentDto> comments) {
        if (item == null) return null;
        ItemDto dto = toItemDto(item);
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(comments != null ? comments : new ArrayList<>());
        return dto;
    }

    public static ItemShortDto toItemShortDto(Item item) {
        if (item == null) return null;
        ItemShortDto dto = new ItemShortDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwner() != null ? item.getOwner().getId() : null);
        return dto;
    }

    public static Item toItem(ItemDto dto, User owner) {
        if (dto == null) return null;
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);
        return item;
    }

    public static void updateItemFields(Item item, ItemDto dto) {
        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getAvailable() != null) item.setAvailable(dto.getAvailable());
    }
}