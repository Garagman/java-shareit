package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void defaultConstructor_shouldCreateEmptyComment() {
        Comment comment = new Comment();

        assertNotNull(comment);
        assertNull(comment.getId());
        assertNull(comment.getText());
        assertNull(comment.getAuthor());
        assertNull(comment.getItem());
        assertNull(comment.getCreated());
    }

    @Test
    void setters_shouldUpdateFields() {
        Comment comment = new Comment();
        User author = new User(1L, "Author", "author@test.com");
        Item item = new Item();
        item.setId(1L);
        LocalDateTime created = LocalDateTime.now();

        comment.setId(1L);
        comment.setText("Test comment");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(created);

        assertEquals(1L, comment.getId());
        assertEquals("Test comment", comment.getText());
        assertEquals(author, comment.getAuthor());
        assertEquals(item, comment.getItem());
        assertEquals(created, comment.getCreated());
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        Comment comment = new Comment();
        comment.setId(1L);

        assertEquals(comment, comment);
    }

    @Test
    void equals_shouldReturnTrueForEqualObjects() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("Test");

        Comment comment2 = new Comment();
        comment2.setId(1L);
        comment2.setText("Test");

        assertEquals(comment1, comment2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentObjects() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(2L);

        assertNotEquals(comment1, comment2);
    }

    @Test
    void hashCode_shouldReturnSameValueForEqualObjects() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");

        String str = comment.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("Test comment"));
    }
}