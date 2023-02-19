package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validators.UserValidator.*;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long userId) {
        User user = users.get(userId);

        if (user != null) {
            return user;
        }
        String errorMessage = String.format("User ID: %s, not exist", userId);
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    @Override
    public User addUser(User user) {
        if (isUserValidated(user)) {
            user.setId(getNewId());
            if (!isNameValid(user)) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info(String.format("New User: %s, was successfully added", user.getLogin()));
            return user;
        }
        String errorMessage = String.format("User: %s, is not valid", user.getName());
        log.error(errorMessage);
        throw new ValidationException(errorMessage);
    }

    @Override
    public User updateUser(User user) {
        if (isUserExist(user) && isUserValidated(user)) {
            users.put(user.getId(), user);
            log.info(String.format("User: %s, was successfully updated", user.getName()));
            return users.get(user.getId());
        }
        String errorMessage = String.format("User: %s, is not exist", user.getName());
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    public void clearUsers() {
        this.users.clear();
    }

    private Long getNewId() {
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
