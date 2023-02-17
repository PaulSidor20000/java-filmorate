package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_ALL_GENRES
            = "SELECT *"
            + " FROM genre g"
            + " ORDER BY g.genre_id";
    private static final String SQL_FIND_GENRE_BY_ID
            = "SELECT *"
            + " FROM genre g"
            + " WHERE g.genre_id = ?";
    private static final String SQL_ADD_GENRE
            = "INSERT INTO genre (GENRE) VALUES(?)";
    private static final String SQL_UPDATE_GENRE
            = "UPDATE genre SET genre=? WHERE genre_id=?";
    private static final String SQL_DELETE_GENRE
            = "DELETE FROM genre WHERE genre_id=?";
    private static final String SQL_FIND_GENRES_OF_FILM
            = "SELECT g.genre_id, g.genre"
            + " FROM films f"
            + " JOIN genre_link gl ON gl.film_id = f.film_id"
            + " JOIN genre g ON g.genre_id = gl.genre_id"
            + " WHERE f.film_id = ?";

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query(SQL_FIND_ALL_GENRES,
                (rs, rowNum) -> setGenre(rs)
        );
    }

    @Override
    public Genre findGenreById(Integer genreId) {
        return jdbcTemplate.queryForObject(SQL_FIND_GENRE_BY_ID,
                (rs, rowNum) -> setGenre(rs),
                genreId
        );
    }

    @Override
    public Genre addGenre(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(SQL_ADD_GENRE, new String[]{"genre_id"});
                    stmt.setString(1, genre.getName());
                    return stmt;
                },
                keyHolder
        );

        return findGenreById(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public Genre updateGenre(Genre genre) {
        jdbcTemplate.update(SQL_UPDATE_GENRE,
                genre.getName(),
                genre.getId()
        );
        return findGenreById(genre.getId());
    }

    @Override
    public void deleteGenre(Genre genre) {
        jdbcTemplate.update(SQL_DELETE_GENRE, genre.getId());
    }

    public List<Genre> findGenresByFilmId(Long filmId) {
        return jdbcTemplate.query(SQL_FIND_GENRES_OF_FILM,
                (rs, rowNum) -> setGenre(rs),
                filmId
        );
    }

    public Genre setGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getObject("genre_id", Integer.class))
                .name(rs.getString("genre"))
                .build();
    }

}
