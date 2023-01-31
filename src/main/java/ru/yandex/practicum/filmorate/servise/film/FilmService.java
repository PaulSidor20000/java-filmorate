package ru.yandex.practicum.filmorate.servise.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);

        film.getLikes().add(user.getId());
        log.info("Like was added, userId: {}, filmId: {}", userId, filmId);
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);

        film.getLikes().remove(user.getId());
        log.info("Like was deleted, userId: {}, filmId: {}", userId, filmId);
        return film;
    }

    public List<Film> findMostPopularFilms(Long count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }

}

