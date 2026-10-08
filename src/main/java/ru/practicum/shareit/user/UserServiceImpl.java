package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    public final ItemRepository itemRepository;

    public UserServiceImpl(UserRepository repository, ItemRepository itemRepository) {
        this.userRepository = repository;
        this.itemRepository = itemRepository;
    }

    @Override
    public UserDto create(NewUserRequest newUser) {
        return UserMapper.mapToUserDto(userRepository.create(UserMapper.mapToUser(newUser)));
    }

    @Override
    public UserDto findById(Long id) {
        return UserMapper.mapToUserDto(userRepository.findById(id));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto update(UpdateUserRequest updateUser, Long userId) {
        User user = userRepository.findById(userId);
        if (updateUser.hasEmail()) {
            userRepository.checkUsageEmail(updateUser.getEmail());
        }
        UserMapper.updateUserFields(user, updateUser);
        return UserMapper.mapToUserDto(userRepository.update(user));
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId);
        userRepository.delete(userId, user.getEmail());
        itemRepository.deleteItemByOwnerId(userId);
    }
}
