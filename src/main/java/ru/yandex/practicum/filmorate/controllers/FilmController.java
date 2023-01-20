package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ControllerParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage storage;
    private final FilmService service;

    @GetMapping
    public List<Film> findAllFilms() {
        return storage.findAllFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Optional<Film> film) {
        return storage.addFilm(film.orElseThrow(ControllerParameterException::new));
    }

    @PutMapping
    public Film updateFilm(@RequestBody Optional<Film> film) {
        return storage.updateFilm(film.orElseThrow(ControllerParameterException::new));
    }

    // получить по id
    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable(value = "id") Optional<Long> filmId) {
        return storage.findFilmById(filmId.orElseThrow(ControllerParameterException::new));
    }

    // пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public Film addLiketoFilm(
            @PathVariable(value = "id") Optional<Long> filmId,
            @PathVariable(value = "userId") Optional<Long> userId
    ) {
        return service.addLiketoFilm(
                filmId.orElseThrow(ControllerParameterException::new),
                userId.orElseThrow(ControllerParameterException::new)
        );
    }

    // пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeOfFilm(
            @PathVariable(value = "id") Optional<Long> filmId,
            @PathVariable(value = "userId") Optional<Long> userId
    ) {

        return service.deleteLikeOfFilm(
                filmId.orElseThrow(ControllerParameterException::new),
                userId.orElseThrow(ControllerParameterException::new)
        );
    }

    // Возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Optional<Long> count
    ) {
        return service.findMostPopularFilms(count.orElseThrow(ControllerParameterException::new));
    }

}