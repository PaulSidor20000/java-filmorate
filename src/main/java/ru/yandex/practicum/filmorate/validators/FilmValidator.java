package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {

    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private FilmValidator() {
    }   // утилитарный класс, конструкторы не нужны

    public static boolean isNameValid(Film film) {
        if (film.getName() != null
                && !film.getName().isEmpty()
        ) {
            log.info("Поле Name - {} - прошло валидацию Film", film.getName());
            return true;
        } else {
            log.info("Поле Name - {} - не прошло валидацию Film", film.getName());
        }
        return false;
    }

    public static boolean isDescriptionValid(Film film) {
        if (film.getDescription() != null
                && film.getDescription().length() <= MAX_DESCRIPTION_LENGTH
        ) {
            log.info("Поле Description - {} - прошло валидацию Film", film.getDescription());
            return true;
        } else {
            log.info("Поле Description - {} - не прошло валидацию Film", film.getDescription());
        }
        return false;
    }

    public static boolean isReleaseDateValid(Film film) {
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isAfter(MOVIE_BIRTHDAY)
        ) {
            log.info("Поле ReleaseDate - {} - прошло валидацию Film", film.getReleaseDate());
            return true;
        } else {
            log.info("Поле ReleaseDate - {} - не прошло валидацию Film", film.getReleaseDate());
        }
        return false;
    }

    public static boolean isDurationValid(Film film) {
        if (film.getDuration() > 0) {
            log.info("Поле Duration - {} - прошло валидацию Film", film.getDuration());
            return true;
        } else {
            log.info("Поле Duration - {} - не прошло валидацию Film", film.getDuration());
        }
        return false;
    }

}