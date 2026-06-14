package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public Item create(ItemDto itemDto, Long ownerId) {
        User owner = userService.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        return itemRepository.save(item);
    }

    @Override
    public Item update(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Item with id " + itemId + " not found for user with id " + ownerId);
        }
        ItemMapper.updateItemFields(item, itemDto);
        return itemRepository.save(item);
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return itemRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.search(text);
    }
}