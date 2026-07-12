package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemDto update(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDto findById(Long itemId, Long userId);

    List<ItemDto> findAllByOwnerId(Long ownerId);

    List<ItemDto> search(String text, Long userId);

    CommentDto addComment(CommentDto commentDto, Long itemId, Long authorId);
}