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
            log.info("Поле Email - {} - прошло валидацию User", user.getEmail());
            return true;
        } else {
            log.info("Поле Email - {} - не прошло валидацию User", user.getEmail());
        }
        return false;
    }

    public static boolean isLoginValid(User user) {
        if (user.getLogin() != null
                && !user.getLogin().isBlank() && !user.getLogin().contains(" ")
        ) {
            log.info("Поле Login - {} - прошло валидацию User", user.getLogin());
            return true;
        } else {
            log.info("Поле Login - {} - не прошло валидацию User", user.getLogin());
        }
        return false;
    }

    public static boolean isNameValid(User user) {
        if (user.getName() != null
                && !user.getName().isEmpty()
        ) {
            log.info("Поле Name - {} - прошло валидацию User", user.getName());
            return true;
        } else {
            log.info("Поле Name - {} - не прошло валидацию User", user.getName());
        }
        return false;
    }

    public static boolean isBirthdayValid(User user) {
        if (user.getBirthday() != null
                && user.getBirthday().isBefore(LocalDate.now())
        ) {
            log.info("Поле Birthday - {} - прошло валидацию User", user.getBirthday());
            return true;
        } else {
            log.info("Поле Birthday - {} - не прошло валидацию User", user.getBirthday());
        }
        return false;
    }

}