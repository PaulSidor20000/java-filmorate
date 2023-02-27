package ru.yandex.practicum.filmorate.servicestests;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

public class TestEnvironment {
    User user1, user2, friend1;
    Film film1, film2;
    MPARating mpa1, mpa2, mpa3, mpa4, mpa5;
    Genre genre1, genre2, genre3, genre4, genre5, genre6;

    @BeforeEach
    void beforeEach() {
        user1 = User.builder()
                .id(1L)
                .name("Jack1")
                .login("user1")
                .email("user1@mail.ru")
                .birthday(LocalDate.parse("2000-10-10"))
                .friends(List.of())
                .build();

        user2 = User.builder()
                .id(2L)
                .name("John2")
                .login("user2")
                .email("user2@mail.ru")
                .birthday(LocalDate.parse("2001-11-15"))
                .friends(List.of())
                .build();

        friend1 = User.builder()
                .id(3L)
                .name("Joe3")
                .login("user3")
                .email("user3@mail.ru")
                .birthday(LocalDate.parse("1999-01-10"))
                .friends(List.of())
                .build();

        mpa1 = MPARating.builder()
                .id(1)
                .name("G")
                .build();

        mpa2 = MPARating.builder()
                .id(2)
                .name("PG")
                .build();

        mpa3 = MPARating.builder()
                .id(3)
                .name("PG-13")
                .build();

        mpa4 = MPARating.builder()
                .id(4)
                .name("R")
                .build();

        mpa5 = MPARating.builder()
                .id(5)
                .name("NC-17")
                .build();

        genre1 = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();

        genre2 = Genre.builder()
                .id(2)
                .name("Драма")
                .build();

        genre3 = Genre.builder()
                .id(3)
                .name("Мультфильм")
                .build();

        genre4 = Genre.builder()
                .id(4)
                .name("Триллер")
                .build();

        genre5 = Genre.builder()
                .id(5)
                .name("Документальный")
                .build();

        genre6 = Genre.builder()
                .id(6)
                .name("Боевик")
                .build();

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

        film2 = Film.builder()
                .id(2L)
                .name("Pulp Fiction")
                .description("Just because you are a character does not mean you have character")
                .releaseDate(LocalDate.parse("1994-05-21"))
                .duration(154)
                .mpa(mpa2)
                .genres(List.of(genre2))
                .likes(List.of())
                .build();

    }

}
