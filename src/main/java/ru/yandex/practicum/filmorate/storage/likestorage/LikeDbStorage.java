package ru.yandex.practicum.filmorate.storage.likestorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mparating.MPARatingDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component("likeDbStorage")
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MPARatingDbStorage mpaRatingDbStorage;
    private final GenreDbStorage genreDbStorage;
    private static final String SQL_ADD_LIKE_TO_FILM =
            "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
    private static final String SQL_DELETE_LIKE_FROM_FILM =
            "DELETE FROM likes WHERE film_id=? AND user_id=?";
    private static final String SQL_GET_USERS_LIKES_FILM =
            "SELECT user_id FROM likes WHERE film_id=?";
    private static final String SQL_GET_TOP_LIKES_FILM =
            "SELECT f.*"
                    + " FROM films f"
                    + " LEFT JOIN likes l ON f.film_id = l.film_id"
                    + " GROUP BY f.film_id"
                    + " ORDER BY COUNT(l.user_id) DESC"
                    + " LIMIT ?";

    @Override
    public boolean addLike(Long filmId, Long userId) {
        int count = jdbcTemplate.update(SQL_ADD_LIKE_TO_FILM,
                filmId,
                userId
        );
        log.info("{} Like(s) have been added", count);

        return count > 0;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        int count = jdbcTemplate.update(SQL_DELETE_LIKE_FROM_FILM,
                filmId,
                userId
        );
        if (count > 0) {
            log.info("{} Like(s) have been deleted", count);

            return true;
        } else {
            throw new IllegalArgumentException("Wrong parameters on Like deleting");
        }
    }

    @Override
    public List<Film> findMostPopularFilms(Long count) {
        return jdbcTemplate.query(SQL_GET_TOP_LIKES_FILM,
                (rs, rowNum) -> setFilm(rs),
                count
        );
    }

    public List<Long> getUsersLikesFilm(Long filmId) {
        return jdbcTemplate.query(SQL_GET_USERS_LIKES_FILM,
                (rs, rowNum) -> setUsersIds(rs),
                filmId
        );
    }

    private Long setUsersIds(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }

    private Film setFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .genres(genreDbStorage.findGenresByFilmId(rs.getLong("film_id")))
                .mpa(mpaRatingDbStorage.findRatingById(rs.getInt("mpa_rating_id")))
                .likes(getUsersLikesFilm(rs.getLong("film_id")))
                .build();
    }

}
