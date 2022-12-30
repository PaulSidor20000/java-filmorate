package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static ru.yandex.practicum.filmorate.validators.UserValidator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int id;

    private final Map<Integer, User> users = new HashMap<>();

    public void clearUsers() {
        this.users.clear();
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User setNewUser(@RequestBody User user) {
        if (isUserValidated(user)) {
            user.setId(getNewId());
            if (!isNameValid(user)) {
                user.setName(user.getLogin());
            }
            log.info("Добавляем нового пользователя {}", user.getLogin());
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("Пользователь {} - не прошёл валидацию", user.getName());
            throw new ValidationException("User is not valid");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (isUserExist(user) && isUserValidated(user)) {
            log.info("Обновляем данные пользователя {}", user.getId());
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("Обновляемый пользователь {} - не прошёл валидацию", user.getId());
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