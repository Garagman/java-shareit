package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private User booker;

    @BeforeEach
    void setUp() {
        User newOwner = new User();
        newOwner.setName("Test Owner");
        newOwner.setEmail("owner@test.com");
        owner = userRepository.save(newOwner);

        User newBooker = new User();
        newBooker.setName("Test Booker");
        newBooker.setEmail("booker@test.com");
        booker = userRepository.save(newBooker);
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
    }

    @Test
    void findById_withNonExistentId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            itemService.findById(999L, owner.getId());
        });
    }

    @Test
    void findById_whenUserIsNotOwner_shouldNotIncludeBookings() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        ItemDto foundItem = itemService.findById(createdItem.getId(), booker.getId());

        assertNotNull(foundItem);
        assertNull(foundItem.getLastBooking());
        assertNull(foundItem.getNextBooking());
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
        assertFalse(updatedItem.getAvailable());
    }

    @Test
    void updateItem_byNonOwner_shouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Name");

        assertThrows(NotFoundException.class, () -> {
            itemService.update(updateDto, createdItem.getId(), booker.getId());
        });
    }

    @Test
    void findAllByOwnerId_shouldReturnItemList() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Item 1");
        itemDto1.setDescription("Description 1");
        itemDto1.setAvailable(true);
        itemService.create(itemDto1, owner.getId(), null);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Item 2");
        itemDto2.setDescription("Description 2");
        itemDto2.setAvailable(true);
        itemService.create(itemDto2, owner.getId(), null);

        List<ItemDto> items = itemService.findAllByOwnerId(owner.getId());

        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    void findAllByOwnerId_withNoItems_shouldReturnEmptyList() {
        User newUser = new User();
        newUser.setName("User Without Items");
        newUser.setEmail("noitems@test.com");
        User savedUser = userRepository.save(newUser);

        List<ItemDto> items = itemService.findAllByOwnerId(savedUser.getId());

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void search_shouldReturnMatchingItems() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemService.create(itemDto, owner.getId(), null);

        List<ItemDto> foundItems = itemService.search("Test", owner.getId());

        assertNotNull(foundItems);
        assertFalse(foundItems.isEmpty());
    }

    @Test
    void search_withEmptyText_shouldReturnEmptyList() {
        List<ItemDto> foundItems = itemService.search("", owner.getId());

        assertNotNull(foundItems);
        assertTrue(foundItems.isEmpty());
    }

    @Test
    void search_withNullText_shouldReturnEmptyList() {
        List<ItemDto> foundItems = itemService.search(null, owner.getId());

        assertNotNull(foundItems);
        assertTrue(foundItems.isEmpty());
    }

    @Test
    void addComment_withPastBooking_shouldReturnCommentDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(createdItem.getId());
        bookingDto.setStart(LocalDateTime.now().minusDays(2));
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));
        BookingDto createdBooking = bookingService.create(bookingDto, booker.getId());
        bookingService.updateStatus(createdBooking.getId(), owner.getId(), true);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        CommentDto savedComment = itemService.addComment(commentDto, createdItem.getId(), booker.getId());

        assertNotNull(savedComment);
        assertNotNull(savedComment.getId());
        assertEquals("Great item!", savedComment.getText());
    }

    @Test
    void addComment_withNonExistentItem_shouldThrowNotFoundException() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        assertThrows(NotFoundException.class, () -> {
            itemService.addComment(commentDto, 999L, owner.getId());
        });
    }

    @Test
    void addComment_withNonExistentAuthor_shouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        assertThrows(NotFoundException.class, () -> {
            itemService.addComment(commentDto, createdItem.getId(), 999L);
        });
    }

    @Test
    void addComment_withoutPastBooking_shouldThrowIllegalArgumentException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        ItemDto createdItem = itemService.create(itemDto, owner.getId(), null);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        assertThrows(IllegalArgumentException.class, () -> {
            itemService.addComment(commentDto, createdItem.getId(), booker.getId());
        });
    }
}