package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User create(UserDto userDto);

    User update(UserDto userDto, Long userId);

    Optional<User> findById(Long userId);

    List<User> findAll();

    void delete(Long userId);
}