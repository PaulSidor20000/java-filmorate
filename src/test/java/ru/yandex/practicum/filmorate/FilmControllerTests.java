package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTests {

    private static final String ASSERT_SIZE = "Wrong actual size";
    private static final String ASSERT_VALUE = "Wrong actual value";
    private static final String ASSERT_NULL = "Null point reference";
    private static final String ASSERT_TROWS_INVALID = "The method should trows ValidationException";
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();

        film = Film.builder()
                .name("Time")
                .description("description")
                .releaseDate(LocalDate.of(2011, Month.JANUARY, 1))
                .duration(100)
                .build();
    }

    @AfterEach
    void afterEach() {
        filmController.clearFilms();
    }

    // create Film
    @Test
    void setNewFilmNormallyAndGetAllFilms() {
        filmController.setNewFilm(film);

        assertEquals(1, filmController.getAllFilms().size(), ASSERT_SIZE);
        assertEquals(film, filmController.getAllFilms().get(0), ASSERT_VALUE);
        assertNotNull(filmController.getAllFilms().get(0), ASSERT_NULL);

        // check film inside storage
        assertEquals(1, filmController.getAllFilms().get(0).getId(), ASSERT_VALUE);
        assertEquals("Time", filmController.getAllFilms().get(0).getName(), ASSERT_VALUE);
        assertEquals("description", filmController.getAllFilms().get(0).getDescription(), ASSERT_VALUE);
        assertEquals(100, filmController.getAllFilms().get(0).getDuration(), ASSERT_VALUE);
        assertEquals(LocalDate.of(2011, Month.JANUARY, 1),
                filmController.getAllFilms().get(0).getReleaseDate(), ASSERT_VALUE);
    }

    @Test
    void setNewFilmWrongName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.setNewFilm(film), ASSERT_TROWS_INVALID);
        assertEquals(0, filmController.getAllFilms().size(), ASSERT_SIZE);
    }

    @Test
    void setNewFilmWrongDescription() {
        film.setDescription("Description more then two hundred symbols" +
                "Description more then two hundred symbols" +
                "Description more then two hundred symbols" +
                "Description more then two hundred symbols" +
                "Description more then two hundred symbols");
        assertThrows(ValidationException.class, () -> filmController.setNewFilm(film), ASSERT_TROWS_INVALID);
        assertEquals(0, filmController.getAllFilms().size(), ASSERT_SIZE);
    }

    @Test
    void setNewFilmWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1800, Month.JANUARY, 1));
        assertThrows(ValidationException.class, () -> filmController.setNewFilm(film), ASSERT_TROWS_INVALID);
        assertEquals(0, filmController.getAllFilms().size(), ASSERT_SIZE);
    }

    @Test
    void setNewFilmWrongDuration() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.setNewFilm(film), ASSERT_TROWS_INVALID);
        assertEquals(0, filmController.getAllFilms().size(), ASSERT_SIZE);
    }

    // update Film
    @Test
    void updateFilmNormallyAndGetAllFilms() {
        filmController.setNewFilm(film);
        film = Film.builder()
                .id(1)
                .name("New Film")
                .description("New Description")
                .releaseDate(LocalDate.of(2022, Month.FEBRUARY, 10))
                .duration(200)
                .build();
        filmController.updateFilm(film);

        assertEquals(1, filmController.getAllFilms().size(), ASSERT_SIZE);
        assertEquals(film, filmController.getAllFilms().get(0), ASSERT_VALUE);
        assertNotNull(filmController.getAllFilms().get(0), ASSERT_NULL);

        // check film inside storage
        assertEquals(1, filmController.getAllFilms().get(0).getId(), ASSERT_VALUE);
        assertEquals("New Film", filmController.getAllFilms().get(0).getName(), ASSERT_VALUE);
        assertEquals("New Description", filmController.getAllFilms().get(0).getDescription(), ASSERT_VALUE);
        assertEquals(200, filmController.getAllFilms().get(0).getDuration(), ASSERT_VALUE);
        assertEquals(LocalDate.of(2022, Month.FEBRUARY, 10),
                filmController.getAllFilms().get(0).getReleaseDate(), ASSERT_VALUE);
    }

    @Test
    void updateFilmWithWrongId() {
        filmController.setNewFilm(film);
        film = Film.builder()
                .id(10)
                .name("New Film")
                .description("New Description")
                .releaseDate(LocalDate.of(2022, Month.FEBRUARY, 10))
                .duration(200)
                .build();

        assertThrows(ValidationException.class, () -> filmController.updateFilm(film), ASSERT_TROWS_INVALID);
        assertEquals(1, filmController.getAllFilms().size(), ASSERT_SIZE);
        assertEquals(1, filmController.getAllFilms().get(0).getId(), ASSERT_SIZE);
    }

}
