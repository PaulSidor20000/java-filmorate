package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.*;
import static ru.yandex.practicum.filmorate.validators.FilmValidator.isDurationValid;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 0L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(Long filmId) {
        Film film = films.get(filmId);

        if (film != null) {
            return film;
        }
        String errorMessage = String.format("Film id: %s, not exist", filmId);
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    @Override
    public Film addFilm(Film film) {
        if (isFilmValidated(film)) {
            film.setId(getNewId());
            films.put(film.getId(), film);
            log.info(String.format("New Film: %s, was successfully added.", film.getName()));
            return film;
        }
        String errorMessage = String.format("Film: %s, is not valid", film.getName());
        log.error(errorMessage);
        throw new ValidationException(errorMessage);
    }

    @Override
    public Film updateFilm(Film film) {
        if (isFilmExist(film) && isFilmValidated(film)) {
            films.put(film.getId(), film);
            log.info(String.format("Film: %s, was successfully updated", film.getName()));
            return film;
        }
        String errorMessage = String.format("Film: %s, is not exist", film.getName());
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    private Long getNewId() {
        return ++id;
    }

    public void clearFilms() {
        this.films.clear();
    }

    private boolean isFilmExist(Film film) {
        return films.containsKey(film.getId());
    }

    private boolean isFilmValidated(Film film) {
        return isNameValid(film)
                && isDescriptionValid(film)
                && isReleaseDateValid(film)
                && isDurationValid(film);
    }

}
