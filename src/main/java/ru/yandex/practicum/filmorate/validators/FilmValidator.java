package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {

    private FilmValidator() {
    }   // утилитарный класс, конструкторы не нужны

    public static boolean isNameValid(Film film) {
        return film.getName() != null
                && !film.getName().isEmpty();
    }

    public static boolean isDescriptionValid(Film film) {
        return film.getDescription() != null
                && film.getDescription().length() >= 200;
    }

    public static boolean isReleaseDateValid(Film film) {
        return film.getReleaseDate() != null
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28));
    }

    public static boolean isDurationValid(Film film) {

        return film.getDuration() != null
                && !film.getDuration().isNegative();
    }

}
