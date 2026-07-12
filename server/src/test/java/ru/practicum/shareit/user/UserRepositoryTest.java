package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void findById_existingUser_shouldReturnUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        User createdUser = userService.create(userDto);

        Optional<User> foundUser = userRepository.findById(createdUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(createdUser.getId(), foundUser.get().getId());
    }

    @Test
    void findById_nonExistingUser_shouldReturnEmpty() {
        Optional<User> foundUser = userRepository.findById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("User 1");
        userDto1.setEmail("user1@example.com");
        userService.create(userDto1);

        UserDto userDto2 = new UserDto();
        userDto2.setName("User 2");
        userDto2.setEmail("user2@example.com");
        userService.create(userDto2);

        List<User> users = userRepository.findAll();

        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    void save_shouldPersistUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
    }

    @Test
    void deleteById_shouldRemoveUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        User createdUser = userService.create(userDto);

        userRepository.deleteById(createdUser.getId());

        Optional<User> foundUser = userRepository.findById(createdUser.getId());
        assertFalse(foundUser.isPresent());
    }
}