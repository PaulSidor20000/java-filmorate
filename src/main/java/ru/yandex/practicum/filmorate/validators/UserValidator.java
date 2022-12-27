package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    private UserValidator() {
    }   // утилитарный класс, конструкторы не нужны

    public static boolean isEmailValid(User user) {
        return user.getEmail() != null
                && !user.getEmail().isEmpty()
                && user.getEmail().contains("@");
    }

    public static boolean isLoginValid(User user) {
        return user.getLogin() != null
                && !user.getLogin().isBlank();
    }

    public static boolean isNameValid(User user) {
        return user.getName() != null
                && !user.getName().isEmpty();
    }

    public static boolean isBirthdayValid(User user) {
        return user.getBirthday() != null
                && user.getBirthday().isBefore(LocalDate.now());
    }

}