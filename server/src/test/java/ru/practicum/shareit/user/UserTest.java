package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void defaultConstructor_shouldCreateEmptyUser() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        User user = new User(1L, "Test User", "test@example.com");

        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void setters_shouldUpdateFields() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        User user = new User(1L, "Test", "test@example.com");

        assertEquals(user, user);
    }

    @Test
    void equals_shouldReturnTrueForEqualObjects() {
        User user1 = new User(1L, "Test", "test@example.com");
        User user2 = new User(1L, "Test", "test@example.com");

        assertEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentObjects() {
        User user1 = new User(1L, "Test", "test@example.com");
        User user2 = new User(2L, "Test", "test@example.com");

        assertNotEquals(user1, user2);
    }

    @Test
    void hashCode_shouldReturnSameValueForEqualObjects() {
        User user1 = new User(1L, "Test", "test@example.com");
        User user2 = new User(1L, "Test", "test@example.com");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toString_shouldContainAllFields() {
        User user = new User(1L, "Test User", "test@example.com");
        String str = user.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("Test User"));
        assertTrue(str.contains("test@example.com"));
    }
}