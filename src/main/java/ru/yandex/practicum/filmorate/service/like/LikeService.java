package ru.yandex.practicum.filmorate.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.likestorage.LikeStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeStorage likeStorage;

    public boolean addLike(Long filmId, Long userId) {
        return likeStorage.addLike(filmId, userId);
    }

    public boolean deleteLike(Long filmId, Long userId) {
        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> findMostPopularFilms(Long count) {
        return likeStorage.findMostPopularFilms(count);
    }

}
