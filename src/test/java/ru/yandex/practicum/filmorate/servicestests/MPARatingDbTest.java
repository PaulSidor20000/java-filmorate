package ru.yandex.practicum.filmorate.servicestests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.mparating.MPARatingService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MPARatingDbTest extends TestEnvironment {
    private final MPARatingService mpaRatingService;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void before() {
        jdbcTemplate.update("DELETE FROM mpa_rating");
        jdbcTemplate.update("ALTER TABLE mpa_rating ALTER COLUMN mpa_rating_id RESTART WITH 1");
        mpaRatingService.addRating(mpa1);
        mpaRatingService.addRating(mpa2);
    }

    @AfterEach
    void afterEach() {
        jdbcTemplate.update("DELETE FROM mpa_rating");
        jdbcTemplate.update("ALTER TABLE mpa_rating ALTER COLUMN mpa_rating_id RESTART WITH 1");
    }

    @Test
    void findAllRatingsTest() {
        List<MPARating> ratings = mpaRatingService.findAllRatings();

        assertThat(ratings, equalTo(List.of(mpa1, mpa2)));
    }

    @Test
    void addRatingTest() {
        MPARating newRating = MPARating.builder()
                .id(3)
                .name("NewRating")
                .build();

        MPARating rating = mpaRatingService.addRating(newRating);

        assertThat(rating, equalTo(newRating));
    }

    @Test
    void updateRatingTest() {
        MPARating updateRating = MPARating.builder()
                .id(1)
                .name("updateRating")
                .build();

        MPARating rating = mpaRatingService.updateRating(updateRating);

        assertThat(rating, equalTo(updateRating));
    }
    @Test
    void findRatingByIdTest() {
        MPARating rating = mpaRatingService.findRatingById(1);

        assertThat(rating, equalTo(mpa1));
    }

}
