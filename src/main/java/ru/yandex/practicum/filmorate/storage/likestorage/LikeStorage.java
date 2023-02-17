package ru.yandex.practicum.filmorate.storage.likestorage;

public interface LikeStorage {
    boolean addLike(Long filmId, Long userId);
    boolean deleteLike(Long filmId, Long userId);

}
