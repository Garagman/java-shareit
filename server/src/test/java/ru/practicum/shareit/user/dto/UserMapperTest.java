package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDto_shouldMapAllFields() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        UserDto dto = UserMapper.toUserDto(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void toUser_shouldMapAllFields() {
        UserDto dto = new UserDto();
        dto.setName("Test User");
        dto.setEmail("test@example.com");

        User user = UserMapper.toUser(dto);

        assertNotNull(user);
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void updateUserFields_shouldUpdateNonNullFields() {
        User user = new User();
        user.setId(1L);
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UserDto updateDto = new UserDto();
        updateDto.setName("New Name");

        UserMapper.updateUserFields(user, updateDto);

        assertEquals("New Name", user.getName());
        assertEquals("old@example.com", user.getEmail());
    }

    @Test
    void toUserDto_withNull_shouldReturnNull() {
        assertNull(UserMapper.toUserDto(null));
    }

    @Test
    void toUser_withNull_shouldReturnNull() {
        assertNull(UserMapper.toUser(null));
    }
}