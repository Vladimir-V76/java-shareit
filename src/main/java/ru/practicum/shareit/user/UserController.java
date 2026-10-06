package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public UserDto findById(
            @Positive(message = "Id должно быть числом положительным")
            @PathVariable Long userId) {
        return userService.findById(userId);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @PatchMapping("/{userId}")
    @ResponseBody
    public UserDto update(
            @Positive(message = "Id должно быть числом положительным")
            @PathVariable Long userId,

            @Valid @RequestBody UpdateUserRequest newUser) {
        return userService.update(newUser, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseBody
    public void delete(
            @Positive(message = "Id должно быть числом положительным")
            @PathVariable Long userId) {
        userService.delete(userId);
    }
}
