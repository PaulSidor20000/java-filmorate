package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
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
    public Film addFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(
            @PathVariable(value = "id") Long filmId
    ) {
        return service.findFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> addLikeToFilm(
            @PathVariable(value = "id") Long filmId,
            @PathVariable(value = "userId") Long userId
    ) {
        service.addLike(filmId, userId);
        return ResponseEntity.ok("Like has been added");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLikeOfFilm(
            @PathVariable(value = "id") Long filmId,
            @PathVariable(value = "userId") Long userId
    ) {
        service.deleteLike(filmId, userId);

        return ResponseEntity.ok("Like has been deleted");
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Long count
    ) {
        return service.findMostPopularFilms(count);
    }

}