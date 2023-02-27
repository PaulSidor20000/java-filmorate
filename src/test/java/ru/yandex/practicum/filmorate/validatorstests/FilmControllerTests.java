package ru.yandex.practicum.filmorate.validatorstests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.like.LikeService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTests {
    private static final String ASSERT_SIZE = "Wrong actual size";
    private static final String ASSERT_VALUE = "Wrong actual value";
    private static final String ASSERT_NULL = "Null point reference";
    private static final String ASSERT_TROWS = "The method should trows an Exception";
    private FilmController filmController;
    InMemoryFilmStorage storage;
    private Film film;

    @BeforeEach
    void beforeEach() {
        storage = new InMemoryFilmStorage();
        filmController = new FilmController(new FilmService(storage, null));

        film = Film.builder()
                .name("Time")
                .description("description")
                .releaseDate(LocalDate.of(2011, Month.JANUARY, 1))
                .duration(100)
                .build();
    }

    @AfterEach
    void afterEach() {
        storage.clearFilms();
    }

    // create Film
    @Test
    void setNewFilmNormallyAndGetAllFilms() {
        filmController.addFilm(film);

        assertEquals(1, filmController.findAllFilms().size(), ASSERT_SIZE);
        assertEquals(film, filmController.findAllFilms().get(0), ASSERT_VALUE);
        assertNotNull(filmController.findAllFilms().get(0), ASSERT_NULL);

        // check film inside storage
        assertEquals(1, filmController.findAllFilms().get(0).getId(), ASSERT_VALUE);
        assertEquals("Time", filmController.findAllFilms().get(0).getName(), ASSERT_VALUE);
        assertEquals("description", filmController.findAllFilms().get(0).getDescription(), ASSERT_VALUE);
        assertEquals(100, filmController.findAllFilms().get(0).getDuration(), ASSERT_VALUE);
        assertEquals(LocalDate.of(2011, Month.JANUARY, 1),
                filmController.findAllFilms().get(0).getReleaseDate(), ASSERT_VALUE);
    }

    @Test
    void setNewFilmWrongName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film), ASSERT_TROWS);
        assertEquals(0, filmController.findAllFilms().size(), ASSERT_SIZE);
    }

    @Test
    void setNewFilmWrongDescription() {
        film.setDescription(
                "Description more then two hundred symbols" +
                "Description more then two hundred symbols" +
                "Description more then two hundred symbols" +
                "Description more then two hundred symbols" +
                "Description more then two hundred symbols"
        );
        assertThrows(ValidationException.class, () -> filmController.addFilm(film), ASSERT_TROWS);
        assertEquals(0, filmController.findAllFilms().size(), ASSERT_SIZE);
    }

    @Test
    void setNewFilmWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1800, Month.JANUARY, 1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film), ASSERT_TROWS);
        assertEquals(0, filmController.findAllFilms().size(), ASSERT_SIZE);
    }

    @Test
    void setNewFilmWrongDuration() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film), ASSERT_TROWS);
        assertEquals(0, filmController.findAllFilms().size(), ASSERT_SIZE);
    }

    // update Film
    @Test
    void updateFilmNormallyAndGetAllFilms() {
        filmController.addFilm(film);
        film = Film.builder()
                .id(1L)
                .name("New Film")
                .description("New Description")
                .releaseDate(LocalDate.of(2022, Month.FEBRUARY, 10))
                .duration(200)
                .build();
        filmController.updateFilm(film);

        assertEquals(1, filmController.findAllFilms().size(), ASSERT_SIZE);
        assertEquals(film, filmController.findAllFilms().get(0), ASSERT_VALUE);
        assertNotNull(filmController.findAllFilms().get(0), ASSERT_NULL);

        // check film inside storage
        assertEquals(1, filmController.findAllFilms().get(0).getId(), ASSERT_VALUE);
        assertEquals("New Film", filmController.findAllFilms().get(0).getName(), ASSERT_VALUE);
        assertEquals("New Description", filmController.findAllFilms().get(0).getDescription(), ASSERT_VALUE);
        assertEquals(200, filmController.findAllFilms().get(0).getDuration(), ASSERT_VALUE);
        assertEquals(LocalDate.of(2022, Month.FEBRUARY, 10),
                filmController.findAllFilms().get(0).getReleaseDate(), ASSERT_VALUE);
    }

    @Test
    void updateFilmWithWrongId() {
        filmController.addFilm(film);
        film = Film.builder()
                .id(10L)
                .name("New Film")
                .description("New Description")
                .releaseDate(LocalDate.of(2022, Month.FEBRUARY, 10))
                .duration(200)
                .build();

        assertThrows(IllegalArgumentException.class, () -> filmController.updateFilm(film), ASSERT_TROWS);
        assertEquals(1, filmController.findAllFilms().size(), ASSERT_SIZE);
        assertEquals(1, filmController.findAllFilms().get(0).getId(), ASSERT_SIZE);
    }

}
