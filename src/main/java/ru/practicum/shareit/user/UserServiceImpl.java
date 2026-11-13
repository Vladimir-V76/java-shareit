package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(NewUserRequest newUser) {
        checkUsageEmail(newUser.getEmail());
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.mapToUser(newUser)));
    }

    @Override
    public UserDto findById(Long id) {
        return UserMapper.mapToUserDto(findUserById(id));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    @Transactional
    public UserDto update(UpdateUserRequest updateUser, Long userId) {
        User user = findUserById(userId);
        if (updateUser.hasEmail()) {
            checkUsageEmail(updateUser.getEmail());
        }
        UserMapper.updateUserFields(user, updateUser);
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        findUserById(userId);
        userRepository.deleteById(userId);
    }

    private void checkUsageEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new DuplicateException("Пользователь с email: " + email + " уже зарегистрирован");
                });
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
    }
}
