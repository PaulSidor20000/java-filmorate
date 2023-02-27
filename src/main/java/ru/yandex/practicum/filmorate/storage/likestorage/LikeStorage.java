package ru.yandex.practicum.filmorate.storage.likestorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    boolean addLike(Long filmId, Long userId);
    boolean deleteLike(Long filmId, Long userId);
    public List<Film> findMostPopularFilms(Long count);

}
