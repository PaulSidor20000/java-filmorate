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
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.likestorage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mparating.MPARatingDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
            = "SELECT f.*, mpa.mpa_rating_name"
            + " FROM films f"
            + " LEFT JOIN mpa_rating mpa ON mpa.mpa_rating_id = f.mpa_rating_id";
    private static final String SQL_FIND_FILM_BY_ID
            = SQL_FIND_ALL_FILMS + " WHERE f.film_id = ?";
    private static final String SQL_ADD_FILM
            = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_FILM
            = "UPDATE FILMS SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA_RATING_ID=? WHERE FILM_ID=?";
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
        Film film = jdbcTemplate.queryForObject(SQL_FIND_FILM_BY_ID,
                (rs, rowNum) -> setFilm(rs),
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
        Film updateFilm;
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

        updateFilm = findFilmById(film.getId());
        if (updateFilm != null) {
            log.info(String.format("Film: %s, was successfully updated", film.getName()));
            return findFilmById(film.getId());
        }
        String errorMessage = String.format("Film: %s, is not exist", film.getName());
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
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

    // Testing of getting MODEL

    private static final String SQL_FIND_MODEL_FILMS
            = "SELECT f.*, mpa.mpa_rating_name, g.genre_name, l.user_id"
            + " FROM films f"
            + " LEFT JOIN mpa_rating mpa ON mpa.mpa_rating_id = f.mpa_rating_id"
            + " LEFT JOIN genre_link gl ON gl.film_id = f.film_id"
            + " LEFT JOIN genre g ON g.genre_id = gl.genre_id"
            + " LEFT JOIN likes l ON l.film_id = f.film_id";

    public List<Film> getaFilm() {
        List<Map<String, Object>> map = jdbcTemplate.queryForList(SQL_FIND_MODEL_FILMS);
        return map.stream()
                .map(this::setFilmModel)
                .collect(Collectors.toList());
    }

    private Film setFilmModel(Map<String, Object> o) {
        return Film.builder()
                .id(Long.valueOf((Integer) o.get("FILM_ID")))
                .name((String) o.get("NAME"))
                .description((String) o.get("DESCRIPTION"))
                .releaseDate(((Date) o.get("RELEASE_DATE")).toLocalDate())
                .duration((Integer) o.get("DURATION"))
                .mpa(setMPAModel((Integer) o.get("MPA_RATING_ID"), (String) o.get("MPA_RATING_NAME")))
                .genres(setGenreModels((Integer) o.get("GENRE_ID"), (String) o.get("GENRE_NAME")))
                .likes(setLikes((Integer) o.get("USER_ID")))
                .build();
    }

    private List<Long> setLikes(Integer userId) {
        return List.of(Long.valueOf(userId));
    }

    private List<Genre> setGenreModels(Integer genreId, String genreName) {
        List<Genre> genres = new ArrayList<>();
        genres.add(
                Genre.builder()
                        .id(genreId)
                        .name(genreName)
                        .build()
        );
        return genres;
    }

    private MPARating setMPAModel(Integer mpaRatingId, String mpaRatingName) {
        return MPARating.builder()
                .id(mpaRatingId)
                .name(mpaRatingName)
                .build();
    }

}
