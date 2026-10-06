package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto create(NewUserRequest newUser) {
        return UserMapper.mapToUserDto(repository.create(UserMapper.mapToUser(newUser)));
    }

    @Override
    public UserDto findById(Long id) {
        return UserMapper.mapToUserDto(repository.findById(id));
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto update(UpdateUserRequest updateUser, Long userId) {
        User user = repository.findById(userId);
        if (updateUser.hasEmail()) {
            repository.checkUsageEmail(updateUser.getEmail());
        }
        UserMapper.updateUserFields(user, updateUser);
        return UserMapper.mapToUserDto(repository.update(user));
    }

    @Override
    public void delete(Long userId) {
        User user = repository.findById(userId);
        repository.delete(userId, user.getEmail());
    }
}
