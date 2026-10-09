package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        return userDto;
    }

    public static User mapToUser(NewUserRequest newUser) {
        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setName(newUser.getName());

        return user;
    }

    public static void updateUserFields(User user, UpdateUserRequest updateUser) {
        if (updateUser.hasEmail()) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.hasName()) {
            user.setName(updateUser.getName());
        }
    }
}
