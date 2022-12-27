package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static ru.yandex.practicum.filmorate.validators.UserValidator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    int id;

    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User setNewUser(@RequestBody User user) {
        if (isUserValidated(user)) {
            user.setId(getNewId());
            if (isNameValid(user)) {
                users.put(user.getId(), user);
            } else {
                user.setName(user.getLogin());
                users.put(user.getId(), user);
            }
            return user;
        } else {
            throw new ValidationException("User is not valid");
        }
    }

    @PutMapping
    public User setUser(@RequestBody User user) {
        if (isUserExist(user)) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("User is not exist");
        }
    }

    private int getNewId() {
        return ++id;
    }

    private boolean isUserExist(User user) {
        return users.containsKey(user.getId());
    }

    private boolean isUserValidated(User user) {
        return isEmailValid(user)
                && isLoginValid(user)
                && isBirthdayValid(user);
    }

}