package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.likestorage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mparating.MPARatingDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.*;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final MPARatingDbStorage mpaRatingDbStorage;
    private static final String SQL_FIND_ALL_FILMS
            = "SELECT f.*, mpa.mpa_rating"
            + " FROM films f"
            + " LEFT JOIN mpa_rating mpa ON mpa.mpa_rating_id = f.mpa_rating_id";
    private static final String SQL_FIND_FILM_BY_ID
            = SQL_FIND_ALL_FILMS + " WHERE f.film_id = ?";
    private static final String SQL_ADD_FILM
            = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_FILM
            = "UPDATE FILMS SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA_RATING_ID=? WHERE FILM_ID=?";
    private static final String SQL_DELETE_FILM
            = "DELETE FROM FILMS WHERE FILM_ID=?";
    private static final String SQL_ADD_GENRE_LINK
            = "MERGE INTO genre_link (film_id, genre_id) KEY (film_id, genre_id) VALUES(?, ?)";
    private static final String SQL_DELETE_GENRES_OF_FILM
            = "DELETE FROM genre_link WHERE film_id=?";

    @Override
    public List<Film> findAllFilms() {
        return jdbcTemplate.query(SQL_FIND_ALL_FILMS,
                (rs, rowNum) -> setFilm(rs)
        );
    }

    @Override
    public Film findFilmById(Long filmId) {
        return jdbcTemplate.queryForObject(SQL_FIND_FILM_BY_ID,
                (rs, rowNum) -> setFilm(rs),
                filmId
        );
    }

    @Override
    public Film addFilm(Film film) {
        if (!isReleaseDateValid(film)) {
            throw new ValidationException("The date of the Film is not valid");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        List<Genre> genres = film.getGenres();

        jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(SQL_ADD_FILM, new String[]{"film_id"});
                    stmt.setString(1, film.getName());
                    stmt.setString(2, film.getDescription());
                    stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                    stmt.setInt(4, film.getDuration());
                    stmt.setInt(5, film.getMpa().getId());
                    return stmt;
                },
                keyHolder
        );

        if (genres != null) {
            jdbcTemplate.update(SQL_DELETE_GENRES_OF_FILM, (Objects.requireNonNull(keyHolder.getKey()).longValue()));
            genres.stream()
                    .distinct()
                    .forEach(genre ->
                            jdbcTemplate.update(SQL_ADD_GENRE_LINK,
                                    (Objects.requireNonNull(keyHolder.getKey()).longValue()),
                                    genre.getId()));
        }

        return findFilmById((Objects.requireNonNull(keyHolder.getKey()).longValue()));
    }

    @Override
    public Film updateFilm(Film film) {
        if (!isReleaseDateValid(film)) {
            throw new ValidationException("The date of the Film is not valid");
        }
        List<Genre> genres = film.getGenres();

        jdbcTemplate.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        if (genres != null) {
            jdbcTemplate.update(SQL_DELETE_GENRES_OF_FILM, film.getId());
            genres.stream()
                    .distinct()
                    .forEach(genre ->
                            jdbcTemplate.update(SQL_ADD_GENRE_LINK,
                                    film.getId(),
                                    genre.getId()));
        }

        return findFilmById(film.getId());
    }

    @Override
    public void deleteFilm(Film film) {
        jdbcTemplate.update(SQL_DELETE_FILM, film.getId());
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
                .likes(likeDbStorage.getUsersLikesFilm(rs.getLong("film_id")))
                .build();
    }

}
