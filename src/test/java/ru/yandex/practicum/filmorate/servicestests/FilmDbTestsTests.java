package ru.yandex.practicum.filmorate.servicestests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mparating.MPARatingService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbTestsTests extends TestEnvironment {
    private final MPARatingService mpaRatingService;
    private final GenreService genreService;
    private final FilmService filmService;
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void before() {
        genreService.addGenre(genre1);
        genreService.addGenre(genre2);
        mpaRatingService.addRating(mpa1);
        mpaRatingService.addRating(mpa2);
        filmService.addFilm(film1);
        filmService.addFilm(film2);
        userService.addUser(user1);
        userService.addUser(user2);
    }

    @AfterEach
    void afterEach() {
        jdbcTemplate.update("DELETE FROM GENRE_LINK");
        jdbcTemplate.update("DELETE FROM LIKES");
        jdbcTemplate.update("DELETE FROM FILMS");
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1");
    }

    @Test
    void findAllFilmsTest() {
        List<Film> films = filmService.findAllFilms();

        assertThat(films, equalTo(List.of(film1, film2)));
    }

    @Test
    void addFilmTest() {
        Film film = Film.builder()
                .name("Time")
                .description("Time will come")
                .releaseDate(LocalDate.parse("1980-07-03"))
                .duration(116)
                .mpa(mpa2)
                .genres(List.of(genre2))
                .likes(List.of())
                .build();

        Film newFilm = filmService.addFilm(film);

        assertThat(newFilm.getId(), equalTo(3L));
        assertThat(newFilm.getName(), equalTo("Time"));
        assertThat(newFilm.getDescription(), equalTo("Time will come"));
        assertThat(newFilm.getReleaseDate(), equalTo(LocalDate.parse("1980-07-03")));
        assertThat(newFilm.getDuration(), equalTo(116));
        assertThat(newFilm.getMpa(), equalTo(mpa2));
        assertThat(newFilm.getGenres(), equalTo(List.of(genre2)));
        assertThat(newFilm.getLikes(), equalTo(List.of()));
    }

    @Test
    void updateFilmTest() {
        Film film = Film.builder()
                .id(1L)
                .name("Time")
                .description("Time will come")
                .releaseDate(LocalDate.parse("1980-07-03"))
                .duration(116)
                .mpa(mpa1)
                .genres(List.of())
                .likes(List.of())
                .build();

        Film newFilm = filmService.updateFilm(film);

        assertThat(newFilm.getId(), equalTo(1L));
        assertThat(newFilm.getName(), equalTo("Time"));
        assertThat(newFilm.getDescription(), equalTo("Time will come"));
        assertThat(newFilm.getReleaseDate(), equalTo(LocalDate.parse("1980-07-03")));
        assertThat(newFilm.getDuration(), equalTo(116));
        assertThat(newFilm.getMpa(), equalTo(mpa1));
        assertThat(newFilm.getGenres(), equalTo(List.of()));
        assertThat(newFilm.getLikes(), equalTo(List.of()));
    }

    @Test
    void deleteFilmByIdTest() {
        boolean isDeleted = filmService.deleteFilmById(1L);

        assertThat(isDeleted, equalTo(true));
        assertThrows(EmptyResultDataAccessException.class, () -> filmService.findFilmById(1L));
    }

    @Test
    void findFilmByIdTest() {
        Film film = filmService.findFilmById(1L);

        assertThat(film, equalTo(film1));
    }

    @Test
    void addLikeTest() {
        film1 = Film.builder()
                .id(1L)
                .name("Back to the Future")
                .description("Seventeen-year-old Marty McFly came home early yesterday. 30 years earlier")
                .releaseDate(LocalDate.parse("1985-07-03"))
                .duration(116)
                .mpa(mpa1)
                .genres(List.of(genre1))
                .likes(List.of(1L))
                .build();

        filmService.addLike(1L, 1L);
        Film film = filmService.findFilmById(1L);

        assertThat(film, equalTo(film1));
    }


    @Test
    void deleteLikeTest() {
        film1 = Film.builder()
                .id(1L)
                .name("Back to the Future")
                .description("Seventeen-year-old Marty McFly came home early yesterday. 30 years earlier")
                .releaseDate(LocalDate.parse("1985-07-03"))
                .duration(116)
                .mpa(mpa1)
                .genres(List.of(genre1))
                .likes(List.of())
                .build();

        filmService.addLike(1L, 1L);
        Film filmWithLike = filmService.findFilmById(1L);

        assertThat(filmWithLike.getLikes(), equalTo(List.of(1L)));

        filmService.deleteLike(1L, 1L);
        Film filmWithoutLike = filmService.findFilmById(1L);

        assertThat(filmWithoutLike, equalTo(film1));
    }


    @Test
    void findMostPopularFilmsTest() {
        filmService.addLike(1L, 1L);
        filmService.addLike(1L, 2L);
        filmService.addLike(2L, 1L);

        Film mostPop = Film.builder()
                .id(1L)
                .name("Back to the Future")
                .description("Seventeen-year-old Marty McFly came home early yesterday. 30 years earlier")
                .releaseDate(LocalDate.parse("1985-07-03"))
                .duration(116)
                .mpa(mpa1)
                .genres(List.of(genre1))
                .likes(List.of(1L, 2L))
                .build();

        List<Film> films = filmService.findMostPopularFilms(1L);
        Film film = filmService.findFilmById(1L);

        assertThat(films.size(), equalTo(1));
        assertThat(film.getLikes(), equalTo(List.of(1L, 2L)));
        assertThat(films.get(0), equalTo(mostPop));
    }


}