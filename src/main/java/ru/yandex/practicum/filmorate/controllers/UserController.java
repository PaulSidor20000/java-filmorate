package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
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
    public User addUser(@Valid @RequestBody User user) {
        return service.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable(value = "id") Long userId) {
        return service.findUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addToFriend(
            @PathVariable(value = "id") Long userId,
            @PathVariable(value = "friendId") Long friendId
    ) {
        service.addToFriend(userId, friendId);
        return ResponseEntity.ok("Friend has been added");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFromFriends(
            @PathVariable(value = "id") Long userId,
            @PathVariable(value = "friendId") Long friendId
    ) {
        service.deleteFromFriends(userId, friendId);
        return ResponseEntity.ok("Friend has been deleted");
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