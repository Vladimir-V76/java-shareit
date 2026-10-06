package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Service
public class UserRepositoryImpl implements UserRepository {

    private static final Map<Long, User> usersById = new HashMap<>();
    private static final Map<String, User> usersByEmail = new HashMap<>();

    @Override
    public User create(User user) {
        long id = 1L;
        if (!usersById.isEmpty()) {
            checkUsageEmail(user.getEmail());
            id = usersById.keySet().stream().max(Comparator.naturalOrder()).orElse(0L);
            id = id + 1;
        }
        user.setId(id);
        usersByEmail.put(user.getEmail(), user);
        usersById.put(id, user);

        return user;
    }

    @Override
    public User findById(Long id) {
        checkUserId(id);
        return usersById.get(id);
    }

    @Override
    public List<User> findAll() {
        return usersById.values().stream().toList();
    }

    @Override
    public User update(User user) {
        usersById.put(user.getId(), user);
        usersByEmail.put(user.getEmail(), user);
        return user;
    }

    @Override
    public void delete(Long userId, String email) {
        usersById.remove(userId);
        usersByEmail.remove(email);
    }

    @Override
    public void checkUsageEmail(String email) {
        if (usersByEmail.containsKey(email)) {
            throw new DuplicateException("Пользователь с email: " + email + " уже зарегистрирован");
        }
    }

    public void checkUserId(Long userId) {
        if (!usersById.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + "не найден");
        }
    }
}
