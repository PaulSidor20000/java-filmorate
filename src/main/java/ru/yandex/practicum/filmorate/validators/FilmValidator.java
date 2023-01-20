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
            log.info("The Name field \"{}\", passed Film validation", film.getName());
            return true;
        } else {
            log.info("The Name field \"{}\", failed Film validation", film.getName());
        }
        return false;
    }

    public static boolean isDescriptionValid(Film film) {
        if (film.getDescription() != null
                && film.getDescription().length() <= MAX_DESCRIPTION_LENGTH
        ) {
            log.info("The Description field \"{}\", passed Film validation", film.getDescription());
            return true;
        } else {
            log.info("The Description field \"{}\", failed Film validation", film.getDescription());
        }
        return false;
    }

    public static boolean isReleaseDateValid(Film film) {
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isAfter(MOVIE_BIRTHDAY)
        ) {
            log.info("The ReleaseDate field \"{}\", passed Film validation", film.getReleaseDate());
            return true;
        } else {
            log.info("The ReleaseDate field \"{}\", failed Film validation", film.getReleaseDate());
        }
        return false;
    }

    public static boolean isDurationValid(Film film) {
        if (film.getDuration() > 0) {
            log.info("The Duration field \"{}\", passed Film validation", film.getDuration());
            return true;
        } else {
            log.info("The Duration field \"{}\", failed Film validation", film.getDuration());
        }
        return false;
    }

}