package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {

    int id;

    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllUsers() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film setNewFilm(@RequestBody Film film) {
        if (isFilmValidated(film)) {
            film.setId(getNewId());

            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Film is not valid");
        }
    }

    @PutMapping
    public Film setFilm(@RequestBody Film film) {
        if (isFilmExist(film)) {

            films.put(film.getId(), film);
            return film;
        } else {
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
