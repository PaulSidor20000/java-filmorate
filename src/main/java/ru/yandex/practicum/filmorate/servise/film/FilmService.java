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
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    // Добавление лайка
    public Film addLiketoFilm(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        Set<Long> likes = film.getLikes();

        if (likes.add(user.getId())) {
            log.info("Film: \"{}\", was successfully liked by User: \"{}\"", film.getName(), user.getName());
            return filmStorage.updateFilm(film);
        }

        log.debug("Film \"{}\" is already liked by User \"{}\"", film.getName(), user.getName());
        throw new IllegalArgumentException(
                String.format("Film %s is already liked by User %s", film.getName(), user.getName()
                ));
    }

    // Удаление лайка
    public Film deleteLikeOfFilm(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        Set<Long> likes = film.getLikes();

        if (likes.remove(user.getId())) {
            log.info("User: \"{}\", was successfully took a like from Film: \"{}\"", user.getName(), film.getName());
            return filmStorage.updateFilm(film);
        }
        log.debug("Film \"{}\" was not liked by User \"{}\"", film.getName(), user.getName());
        throw new IllegalArgumentException(
                String.format("Film %s was not liked by User %s", film.getName(), user.getName()
                ));
    }

    // Вывод наиболее популярных фильмов по количеству лайков
    public List<Film> findMostPopularFilms(Long count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }

}

