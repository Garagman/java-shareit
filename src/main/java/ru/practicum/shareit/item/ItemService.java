package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item create(ItemDto itemDto, Long ownerId);

    Item update(ItemDto itemDto, Long itemId, Long ownerId);

    Optional<Item> findById(Long itemId);

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> search(String text);
}