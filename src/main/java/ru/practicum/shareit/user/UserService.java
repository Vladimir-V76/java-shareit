package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest newUser);
    UserDto findById(Long id);
    List<UserDto> findAll();
    UserDto update(UpdateUserRequest updateUser, Long userId);
    void delete(Long id);
}
