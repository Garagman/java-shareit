package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toCommentDto_shouldMapAllFields() {
        User author = new User();
        author.setId(1L);
        author.setName("Test Author");

        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        CommentDto dto = CommentMapper.toCommentDto(comment);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test comment", dto.getText());
        assertEquals("Test Author", dto.getAuthorName());
        assertNotNull(dto.getCreated());
    }

    @Test
    void toComment_shouldMapAllFields() {
        CommentDto dto = new CommentDto();
        dto.setText("Test comment");

        User author = new User();
        author.setId(1L);

        Item item = new Item();
        item.setId(1L);

        Comment comment = CommentMapper.toComment(dto, item, author);

        assertNotNull(comment);
        assertEquals("Test comment", comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(author, comment.getAuthor());
        assertNotNull(comment.getCreated());
    }

    @Test
    void toCommentDto_withNull_shouldReturnNull() {
        assertNull(CommentMapper.toCommentDto(null));
    }

    @Test
    void toComment_withNull_shouldReturnNull() {
        assertNull(CommentMapper.toComment(null, new Item(), new User()));
    }
}