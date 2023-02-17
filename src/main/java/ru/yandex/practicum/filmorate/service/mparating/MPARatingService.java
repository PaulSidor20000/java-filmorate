package ru.yandex.practicum.filmorate.service.mparating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.mparating.MPARatingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPARatingService {
    private final MPARatingStorage mpaRatingStorage;

    public List<MPARating> findAllRatings() {
        return mpaRatingStorage.findAllRatings();
    }

    public MPARating addRating(MPARating mpaRating) {
        return mpaRatingStorage.addRating(mpaRating);
    }

    public MPARating updateRating(MPARating mpaRating) {
        return mpaRatingStorage.updateRating(mpaRating);
    }

    public MPARating findRatingById(Integer mpaRatingId) {
        return mpaRatingStorage.findRatingById(mpaRatingId);
    }
}
