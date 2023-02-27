package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public List<Genre> findAllGenres();

    public Genre findGenreById(Integer genreId);

    public Genre addGenre(Genre genre);

    public Genre updateGenre(Genre genre);

    public void deleteGenre(Genre genre);

}
