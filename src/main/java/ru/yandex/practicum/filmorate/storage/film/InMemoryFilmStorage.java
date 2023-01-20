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
    public Film addFilm(Film film) {
        if (isFilmValidated(film)) {
            film.setId(getNewId());
            films.put(film.getId(), film);
            log.info("New Film: \"{}\" {}, was successfully added.", film.getName(), film.getReleaseDate().getYear());
            return film;
        }

        log.error("Film: \"{}\", failed validation", film.getName());
        throw new ValidationException(String.format("Film: %s, is not valid", film.getName()));
    }

    @Override
    public Film findFilmById(Long filmId) {
        if (films.containsKey(filmId) && films.get(filmId) != null) {
            return films.get(filmId);
        }

        throw new IllegalArgumentException(String.format("Film with ID: %s, not exist or null pointing", filmId));
    }

    @Override
    public Film updateFilm(Film film) {
        if (isFilmExist(film) && isFilmValidated(film)) {
            films.put(film.getId(), film);
            log.info("Film: \"{}\", was successfully updated", film.getName());
            return film;
        }

        log.error("Film: \"{}\", not found for updating", film.getName());
        throw new IllegalArgumentException(String.format("Film: %s, is not exist", film.getName()));
    }

    @Override
    public void deleteFilm(Film film) {
        if (isFilmExist(film)) {
            films.remove(film.getId());
            log.info("Film: \"{}\", was successfully deleted.", film.getName());
        }

        log.error("Film: \"{}\", not found for deleting", film.getName());
        throw new ValidationException(String.format("Film: %s, is not exist", film.getName()));
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
