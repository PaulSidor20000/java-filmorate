package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> findAllUsers() {
        return service.findAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        return service.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable(value = "id") Long userId) {
        return service.findUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(
            @PathVariable(value = "id") Long userId,
            @PathVariable(value = "friendId") Long friendId
    ) {
        return service.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriends(
            @PathVariable(value = "id") Long userId,
            @PathVariable(value = "friendId") Long friendId
    ) {
        return service.deleteFromFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable(value = "id") Long userId) {
        return service.findFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable(value = "id") Long userId,
            @PathVariable(value = "otherId") Long otherId
    ) {
        return service.findCommonFriends(userId, otherId);
    }

}