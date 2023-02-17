package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping
    public List<Genre> findAllGenres() {
        return service.findAllGenres();
    }

    @PostMapping
    public Genre addGenre(@Valid @RequestBody Genre genre) {
        return service.addGenre(genre);
    }

    @PutMapping
    public Genre updateGenre(@Valid @RequestBody Genre genre) {
        return service.updateGenre(genre);
    }

    @GetMapping("/{id}")
    public Genre findGenreById(@PathVariable(value = "id") Integer genreId) {
        return service.findGenreById(genreId);

    }

}
