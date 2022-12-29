package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    int id;

    private final Map<Integer, Film> films = new HashMap<>();

    public void clearFilms() {
        this.films.clear();
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film setNewFilm(@RequestBody Film film) {
        if (isFilmValidated(film)) {
            film.setId(getNewId());
            log.info("Добавляем новый фильм {}, {} года.", film.getName(), film.getReleaseDate().getYear());
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Новый фильм {} - не прошёл валидацию", film.getName());
            throw new ValidationException("Film is not valid");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (isFilmExist(film) && isFilmValidated(film)) {
            log.info("Обновляем фильм {}, {}.", film.getName(), film.getName());
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Обновляемый фильм {} - не прошёл валидацию", film.getName());
            throw new ValidationException("Film is not exist");
        }
    }

    private int getNewId() {
        return ++id;
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
