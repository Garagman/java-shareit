package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser_shouldReturnUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        User createdUser = userService.create(userDto);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("Test User", createdUser.getName());
        assertEquals("test@example.com", createdUser.getEmail());
    }

    @Test
    void createUser_withExistingEmail_shouldThrowConflictException() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("User 1");
        userDto1.setEmail("duplicate@example.com");
        userService.create(userDto1);

        UserDto userDto2 = new UserDto();
        userDto2.setName("User 2");
        userDto2.setEmail("duplicate@example.com");

        assertThrows(ConflictException.class, () -> {
            userService.create(userDto2);
        });
    }

    @Test
    void updateUser_shouldUpdateFields() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        User createdUser = userService.create(userDto);

        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@example.com");

        User updatedUser = userService.update(updateDto, createdUser.getId());

        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void findById_shouldReturnUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        User createdUser = userService.create(userDto);

        User foundUser = userService.findById(createdUser.getId()).orElseThrow();

        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
    }

    @Test
    void findById_withNonExistentId_shouldReturnEmpty() {
        assertTrue(userService.findById(999L).isEmpty());
    }
}