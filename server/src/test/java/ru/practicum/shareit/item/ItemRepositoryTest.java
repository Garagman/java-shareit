package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    private User owner;

    @BeforeEach
    void setUp() {
        User newUser = new User();
        newUser.setName("Test Owner");
        newUser.setEmail("owner@test.com");
        owner = userRepository.save(newUser);
    }

    @Test
    void findAllByOwnerId_shouldReturnItems() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemService.create(itemDto, owner.getId(), null);

        List<ru.practicum.shareit.item.model.Item> items = itemRepository.findAllByOwnerId(owner.getId());

        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    void search_shouldReturnMatchingItems() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemService.create(itemDto, owner.getId(), null);

        List<ru.practicum.shareit.item.model.Item> items = itemRepository.search("Test");

        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    void search_withDescriptionMatch_shouldReturnItems() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Unique Description");
        itemDto.setAvailable(true);
        itemService.create(itemDto, owner.getId(), null);

        List<ru.practicum.shareit.item.model.Item> items = itemRepository.search("Unique");

        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    void search_withNoMatch_shouldReturnEmptyList() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemService.create(itemDto, owner.getId(), null);

        List<ru.practicum.shareit.item.model.Item> items = itemRepository.search("NonExistent");

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void search_withUnavailableItem_shouldNotReturnIt() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(false);
        itemService.create(itemDto, owner.getId(), null);

        List<ru.practicum.shareit.item.model.Item> items = itemRepository.search("Test");

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void findAllByRequestId_shouldReturnItems() {
        List<ru.practicum.shareit.item.model.Item> items = itemRepository.findAllByRequestId(999L);

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }
}