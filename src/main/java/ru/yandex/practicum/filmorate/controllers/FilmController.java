package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.film.FilmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> findAllFilms() {
        return service.findAllFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable(value = "id") Long filmId) {
        return service.findFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLiketoFilm(
            @PathVariable(value = "id") Long filmId,
            @PathVariable(value = "userId") Long userId
    ) {
        return service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeOfFilm(
            @PathVariable(value = "id") Long filmId,
            @PathVariable(value = "userId") Long userId
    ) {
        return service.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Long count
    ) {
        return service.findMostPopularFilms(count);
    }

}