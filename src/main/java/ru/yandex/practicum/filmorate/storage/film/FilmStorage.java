package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAllFilms();

    Film findFilmById(Long filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);
    boolean deleteFilm(Long filmId);

}
