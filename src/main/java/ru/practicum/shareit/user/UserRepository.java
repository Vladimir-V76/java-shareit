package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User create(User user);

    User findById(Long id);

    List<User> findAll();

    User update(User user);

    void delete(Long id, String email);

    void checkUsageEmail(String email);

    void checkUserId(Long userId);
}
