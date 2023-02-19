package ru.yandex.practicum.filmorate.storage.likestorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Primary
@RequiredArgsConstructor
public class InMemoryLikeStorage implements LikeStorage {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public boolean addLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);

        if (film.getLikes() != null && user != null) {
            film.getLikes().add(user.getId());
            log.info("Like was added, userId: {}, filmId: {}", userId, filmId);
            return true;
        }
        log.debug("Failed Like adding, userId: {}, filmId: {}", userId, filmId);

        return false;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);

        if (film.getLikes().remove(user.getId())) {
            log.info("Like was deleted, userId: {}, filmId: {}", userId, filmId);
            return true;
        }
        log.debug("Failed Like deleting, userId: {}, filmId: {}", userId, filmId);

        return false;
    }

    @Override
    public List<Film> findMostPopularFilms(Long count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }

}
