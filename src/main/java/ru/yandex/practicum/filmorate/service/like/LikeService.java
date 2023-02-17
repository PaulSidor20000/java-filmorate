package ru.yandex.practicum.filmorate.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likestorage.LikeStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;


    public boolean addLike(Long filmId, Long userId) {
        return likeStorage.addLike(filmId, userId);
    }

    public boolean deleteLike(Long filmId, Long userId) {
        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> findMostPopularFilms(Long count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }

}
