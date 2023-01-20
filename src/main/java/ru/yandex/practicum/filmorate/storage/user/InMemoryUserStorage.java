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
@Component
public class InMemoryUserStorage implements UserStorage {
    private Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long userId) {
        return new ArrayList<>(users.values()).stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("User with ID: %s, not exist or null pointing", userId)
                ));
    }

    // fixme Вариант выше(findUserById), по модному через стрим, а ниже явно работает быстрее стримов
    // fixme Какой findUserById оставить?

    /*  public User findUserById(Long userId) {
      if (users.containsKey(userId) && users.get(userId) != null) {
          return users.get(userId);
      }
      throw new IllegalArgumentException(String.format("User with ID: %s, not exist or null pointing", userId));
  }*/

    @Override
    public User addUser(User user) {
        if (isUserValidated(user)) {
            user.setId(getNewId());
            if (!isNameValid(user)) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("New User: \"{}\", was successfully added", user.getLogin());
            return users.get(user.getId());
        }
        log.error("User: \"{}\", is not valid", user.getName());
        throw new ValidationException(String.format("User: %s, is not valid", user.getName()));
    }

    @Override
    public User updateUser(User user) {
        if (isUserExist(user) && isUserValidated(user)) {
            users.put(user.getId(), user);
            log.info("User: \"{}\", was successfully updated", user.getName());
            return users.get(user.getId());
        }
        log.error("User: \"{}\", not found for updating", user.getName());
        throw new IllegalArgumentException(String.format("User: %s, is not exist", user.getName()));
    }

    @Override
    public void deleteUser(User user) {
        if (isUserExist(user)) {
            users.remove(user.getId());
            log.info("User: \"{}\", was successfully deleted", user.getName());
        }
        log.error("User: \"{}\", not found for deleting", user.getName());
        throw new ValidationException(String.format("User: %s, is not exist", user.getName()));
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
