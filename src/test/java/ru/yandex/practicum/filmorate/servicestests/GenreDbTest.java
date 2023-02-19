package ru.yandex.practicum.filmorate.servicestests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbTest extends TestEnvironment {
    private final GenreService genreService;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void before() {
        jdbcTemplate.update("DELETE FROM GENRE");
        jdbcTemplate.update("ALTER TABLE GENRE ALTER COLUMN GENRE_ID RESTART WITH 1");
        genreService.addGenre(genre1);
        genreService.addGenre(genre2);
    }

    @AfterEach
    void after() {
        jdbcTemplate.update("DELETE FROM GENRE");
        jdbcTemplate.update("ALTER TABLE GENRE ALTER COLUMN GENRE_ID RESTART WITH 1");
    }

    @Test
    void findAllGenresTest() {
        List<Genre> genres = genreService.findAllGenres();

        assertThat(genres, equalTo(List.of(genre1, genre2)));
    }

    @Test
    void addGenreTest() {
        Genre genre = Genre.builder()
                .id(3)
                .name("newGenre")
                .build();

        Genre newGenre = genreService.addGenre(genre);

        assertThat(newGenre, equalTo(genre));
    }

    @Test
    void updateGenreTest() {
        Genre genre = Genre.builder()
                .id(1)
                .name("updateGenre")
                .build();

        Genre updateGenre = genreService.updateGenre(genre);

        assertThat(updateGenre, equalTo(genre));
    }

    @Test
    void findGenreByIdTest() {
        Genre genre = genreService.findGenreById(1);

        assertThat(genre, equalTo(genre1));
    }

}

