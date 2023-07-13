package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    String delete(Integer id);

    List<Film> allFilms();

    Film getTargetFilm(Integer id);

    Mpa getTargetMpa(Integer mpaId);

    List<Mpa> getAllMpa();

    Genre getTargetGenre(Integer genreId);

    List<Genre> getAllGenres();
}
