package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceImplTest {

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
    void createItem_shouldReturnItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        assertNotNull(createdItem);
        assertNotNull(createdItem.getId());
        assertEquals("Test Item", createdItem.getName());
        assertEquals("Test Description", createdItem.getDescription());
        assertTrue(createdItem.getAvailable());
    }

    @Test
    void createItem_withNonExistentOwner_shouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        assertThrows(NotFoundException.class, () -> {
            itemService.create(itemDto, 999L, null);
        });
    }

    @Test
    void findById_shouldReturnItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        ItemDto foundItem = itemService.findById(createdItem.getId(), owner.getId());

        assertNotNull(foundItem);
        assertEquals(createdItem.getId(), foundItem.getId());
        assertEquals("Test Item", foundItem.getName());
    }

    @Test
    void findById_withNonExistentId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            itemService.findById(999L, owner.getId());
        });
    }

    @Test
    void updateItem_shouldUpdateFields() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Name");
        updateDto.setAvailable(false);

        ItemDto updatedItem = itemService.update(updateDto, createdItem.getId(), owner.getId());

        assertEquals("Updated Name", updatedItem.getName());
        assertEquals("Test Description", updatedItem.getDescription());
        assertFalse(updatedItem.getAvailable());
    }

    @Test
    void updateItem_byNonOwner_shouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@test.com");
        User savedAnotherUser = userRepository.save(anotherUser);

        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Name");

        assertThrows(NotFoundException.class, () -> {
            itemService.update(updateDto, createdItem.getId(), savedAnotherUser.getId());
        });
    }
}