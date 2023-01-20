package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ControllerParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserStorage storage;
    private final UserService service;

    @GetMapping
    public List<User> getAllUsers() {
        return storage.findAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody Optional<User> user) {
        return storage.addUser(user.orElseThrow(ControllerParameterException::new));
    }

    @PutMapping
    public User updateUser(@RequestBody Optional<User> user) {
        return storage.updateUser(user.orElseThrow(ControllerParameterException::new));
    }

    // получить по id
    @GetMapping("/{id}")
    public User findUserById(@PathVariable(value = "id") Optional<Long> userId) {
        return storage.findUserById(userId.orElseThrow(ControllerParameterException::new));
    }

    // добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(
            @PathVariable(value = "id") Optional<Long> userId,
            @PathVariable(value = "friendId") Optional<Long> friendId
    ) {
        return service.addToFriends(
                userId.orElseThrow(ControllerParameterException::new),
                friendId.orElseThrow(ControllerParameterException::new)
        );
    }

    // удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriends(
            @PathVariable(value = "id") Optional<Long> userId,
            @PathVariable(value = "friendId") Optional<Long> friendId
    ) {
        return service.deleteFromFriends(
                userId.orElseThrow(ControllerParameterException::new),
                friendId.orElseThrow(ControllerParameterException::new)
        );
    }

    // возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable(value = "id") Optional<Long> userId) {
        return service.findFriends(userId.orElseThrow(ControllerParameterException::new));
    }

    // список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable(value = "id") Optional<Long> userId,
            @PathVariable(value = "otherId") Optional<Long> otherId
    ) {
        return service.findCommonFriends(
                userId.orElseThrow(ControllerParameterException::new),
                otherId.orElseThrow(ControllerParameterException::new)
        );
    }

}