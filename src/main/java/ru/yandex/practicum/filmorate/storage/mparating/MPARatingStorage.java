package ru.yandex.practicum.filmorate.storage.mparating;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

public interface MPARatingStorage {
    List<MPARating> findAllRatings();

    MPARating findRatingById(Integer mpaRatingId);

    MPARating addRating(MPARating mpaRating);

    MPARating updateRating(MPARating mpaRating);

    void deleteRating(MPARating mpaRating);
}
