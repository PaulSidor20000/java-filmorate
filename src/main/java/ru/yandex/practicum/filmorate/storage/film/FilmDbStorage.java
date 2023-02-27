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
import ru.yandex.practicum.filmorate.storage.mparating.MPARatingDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.*;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MPARatingDbStorage mpaRatingDbStorage;
    private static final String SQL_FIND_MODEL_FILMS
            = "SELECT f.*,"
            + " mpa.mpa_rating_name,"
            + " GROUP_CONCAT (DISTINCT g.genre_id ORDER BY g.genre_id) AS genre_ids,"
            + " GROUP_CONCAT (DISTINCT g.genre_name ORDER BY g.genre_id) AS genre_names,"
            + " GROUP_CONCAT (DISTINCT l.user_id ORDER BY l.user_id) AS user_ids"
            + " FROM films f"
            + " LEFT JOIN mpa_rating mpa ON mpa.mpa_rating_id = f.mpa_rating_id"
            + " LEFT JOIN genre_link gl ON gl.film_id = f.film_id"
            + " LEFT JOIN genre g ON g.genre_id = gl.genre_id"
            + " LEFT JOIN likes l ON l.film_id = f.film_id"
            + " GROUP BY f.film_id";
    private static final String SQL_FIND_MODEL_FILM_BY_ID
            = SQL_FIND_MODEL_FILMS
            + " HAVING f.film_id = ?";
    private static final String SQL_ADD_FILM
            = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_FILM
            = "UPDATE FILMS SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA_RATING_ID=? WHERE FILM_ID=?";
    private static final String SQL_DELETE_FILM_BY_ID
            = "DELETE FROM films WHERE film_id=?";
    private static final String SQL_ADD_GENRE_LINK
            = "MERGE INTO genre_link (film_id, genre_id) KEY (film_id, genre_id) VALUES(?, ?)";
    private static final String SQL_DELETE_GENRES_OF_FILM
            = "DELETE FROM genre_link WHERE film_id=?";
    private static final String SQL_DELETE_LIKES_OF_FILM
            = "DELETE FROM likes WHERE film_id=?";

    @Override
    public List<Film> findAllFilms() {
        return jdbcTemplate.query(SQL_FIND_MODEL_FILMS,
                this::setFilm
        );
    }

    @Override
    public Film findFilmById(Long filmId) {
        Film film = jdbcTemplate.queryForObject(SQL_FIND_MODEL_FILM_BY_ID,
                this::setFilm,
                filmId
        );
        if (film != null) {
            return film;
        }
        String errorMessage = String.format("Film id: %s, not exist", filmId);
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    @Override
    public Film addFilm(Film film) {
        if (!isReleaseDateValid(film)) {
            throw new ValidationException("The date of the Film is not valid");
        }
        Film newFilm;
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

        newFilm = findFilmById((Objects.requireNonNull(keyHolder.getKey()).longValue()));
        if (newFilm != null) {
            log.info(String.format("New Film: %s, was successfully added.", newFilm.getName()));

            return newFilm;
        }
        String errorMessage = String.format("Film: %s, is not valid", film.getName());
        log.error(errorMessage);
        throw new ValidationException(errorMessage);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!isReleaseDateValid(film)) {
            throw new ValidationException("The date of the Film is not valid");
        }
        Film updateFilm;
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

        updateFilm = findFilmById(film.getId());
        if (updateFilm != null) {
            log.info(String.format("Film: %s, was successfully updated", film.getName()));
            return findFilmById(film.getId());
        }
        String errorMessage = String.format("Film: %s, is not exist", film.getName());
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    @Override
    public boolean deleteFilm(Long filmId) {
        jdbcTemplate.update(SQL_DELETE_GENRES_OF_FILM, filmId);
        jdbcTemplate.update(SQL_DELETE_LIKES_OF_FILM, filmId);
        if (jdbcTemplate.update(SQL_DELETE_FILM_BY_ID, filmId) == 1) {
            log.info(String.format("Film: %s, was deleted", filmId));
            return true;
        }
        log.error(String.format("Failed to delete Film: %s", filmId));
        return false;
    }

    private Film setFilm(ResultSet rs, int rowSet) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaRatingDbStorage.setMPARating(rs, rowSet))
                .genres(setGenres(rs))
                .likes(setLikes(rs))
                .build();
    }

    private List<Genre> setGenres(ResultSet rs) throws SQLException {
        if (rs.getString("genre_ids") == null
                || rs.getString("genre_names") == null) {
            return Collections.emptyList();
        }
        List<Genre> genres = new ArrayList<>();
        String[] genresIds = (rs.getString("genre_ids")).split(",");
        String[] genresNames = (rs.getString("genre_names")).split(",");

        IntStream
                .range(0, genresIds.length)
                .forEach(i -> genres.add(
                        Genre.builder()
                                .id(Integer.valueOf(genresIds[i]))
                                .name(genresNames[i])
                                .build()));
        return genres;
    }

    private List<Long> setLikes(ResultSet rs) throws SQLException {
        if (rs.getString("user_ids") == null) {
            return Collections.emptyList();
        }
        String[] likes = rs.getString("user_ids").split(",");

        return Arrays.stream(likes)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

}
