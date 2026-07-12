package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private User author;
    private Long itemId;

    @BeforeEach
    void setUp() {
        User newOwner = new User();
        newOwner.setName("Owner");
        newOwner.setEmail("owner@test.com");
        owner = userRepository.save(newOwner);

        User newAuthor = new User();
        newAuthor.setName("Author");
        newAuthor.setEmail("author@test.com");
        author = userRepository.save(newAuthor);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemId = itemService.create(itemDto, owner.getId(), null).getId();
    }

    @Test
    void findAllByItemId_shouldReturnComments() {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        assertNotNull(comments);
    }

    @Test
    void findAllByItemIdIn_shouldReturnComments() {
        List<Comment> comments = commentRepository.findAllByItemIdIn(List.of(itemId));

        assertNotNull(comments);
    }

    @Test
    void findAllByItemId_withNoComments_shouldReturnEmptyList() {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }
}