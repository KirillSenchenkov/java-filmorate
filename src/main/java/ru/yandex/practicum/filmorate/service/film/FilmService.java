package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmService {
    String addLike(Integer filmId, Integer userId);

    String deleteLike(Integer filmId, Integer userId);

    List<Film> getFilmByPopularity(Integer count);

    Mpa getTargetMpa(Integer mpaId);

    List<Mpa> getAllMpa();

    Genre getTargetGenre(Integer genreId);

    List<Genre> getAllGenres();

    Film create(Film film);

    Film update(Film film);

    String delete(Integer id);

    List<Film> allFilms();

    Film getTargetFilm(Integer id);
}
