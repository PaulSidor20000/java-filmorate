package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.sql.SQLException;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/testdata.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/reset-data.sql")
class MainTest {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;

    @Test
    void test() throws SQLException {
        System.out.println(filmDbStorage.findAllFilms() + "\n");
        System.out.println(filmDbStorage.findFilmById(1L));
    }
}
