package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    private UserValidator() {
    }   // утилитарный класс, конструкторы не нужны

    public static boolean isEmailValid(User user) {
        if (user.getEmail() != null
                && !user.getEmail().isEmpty()
                && user.getEmail().contains("@")
        ) {
            log.info("The Email field \"{}\", passed User validation", user.getEmail());
            return true;
        } else {
            log.info("The Email field \"{}\", failed Film validation", user.getEmail());
        }
        return false;
    }

    public static boolean isLoginValid(User user) {
        if (user.getLogin() != null
                && !user.getLogin().isBlank() && !user.getLogin().contains(" ")
        ) {
            log.info("The Login field \"{}\", passed User validation", user.getLogin());
            return true;
        } else {
            log.info("The Login field \"{}\", failed Film validation", user.getLogin());
        }
        return false;
    }

    public static boolean isNameValid(User user) {
        if (user.getName() != null
                && !user.getName().isEmpty()
        ) {
            log.info("The Name field \"{}\", passed User validation", user.getName());
            return true;
        } else {
            log.info("The Name field \"{}\", failed Film validation", user.getName());
        }
        return false;
    }

    public static boolean isBirthdayValid(User user) {
        if (user.getBirthday() != null
                && user.getBirthday().isBefore(LocalDate.now())
        ) {
            log.info("The Birthday field \"{}\", passed User validation", user.getBirthday());
            return true;
        } else {
            log.info("The Birthday field \"{}\", failed Film validation", user.getBirthday());
        }
        return false;
    }

}